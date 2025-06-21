package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.BlogResponse;
import com.example.bdhv_itclub.dto.request.BlogRequest;
import com.example.bdhv_itclub.entity.Blog;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.BlogRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.BlogService;
import com.example.bdhv_itclub.utils.GlobalUtil;
import com.example.bdhv_itclub.utils.UploadFileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Transactional
@Service
public class BlogServiceImpl implements BlogService {
    private final ModelMapper modelMapper;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final UploadFileUtil uploadFile;

    public BlogServiceImpl(ModelMapper modelMapper, BlogRepository blogRepository, UserRepository userRepository, UploadFileUtil uploadFile) {
        this.modelMapper = modelMapper;
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.uploadFile = uploadFile;
    }

    // Ok
    @Override
    public String view(Integer blogId) {
        Blog blog = blogRepository.findByIdAndIsApprovedTrue(blogId).orElseThrow(() -> new NotFoundException("Mã bài đăng không tồn tại hoặc bài đăng chưa được duyệt"));
        int view = blog.getNumberOfViews();
        blog.setNumberOfViews(++view);
        blogRepository.save(blog);
        return "Cập nhật lượt xem thành công";
    }

    // Ok
    @Override
    public Page<BlogResponse> getAllBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return blogRepository.findAll(pageable).map(this::convertToBlogResponse);
    }

    // Ok
    @Override
    public Page<BlogResponse> getAllApprovedBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return blogRepository.findAllByIsApprovedTrue(pageable).map(this::convertToBlogResponse);
    }

    // Ok
    @Override
    public Page<BlogResponse> getAllByUser(Integer userId, int page, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Mã người dùng không tồn tại"));
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return blogRepository.findAllByUserAndIsApprovedTrue(user, pageable).map(this::convertToBlogResponse);
    }

    // Ok
    @Override
    public BlogResponse getBySlug(String slug) {
        Blog blog = blogRepository.findBySlugAndIsApprovedTrue(slug).orElseThrow(() -> new NotFoundException("Slug không tồn tại hoặc bài viết chưa được duyệt"));
        return convertToBlogResponse(blog);
    }

    // Ok
    @Override
    public String checkBlogAuthor(Integer blogId, Integer userId) {
        Blog blog = blogRepository.findByIdAndIsApprovedTrue(blogId).orElseThrow(() -> new NotFoundException("Mã bài đăng không tồn tại hoặc bài đăng chưa được duyệt"));
        if (!Objects.equals(userId, blog.getUser().getId())) {
            throw new AccessDeniedException("Chức năng này chỉ dành cho tác giả bài viết");
        }
        return "SUCCESS";
    }

    // Ok
    @Override
    public BlogResponse save(BlogRequest blogRequest, MultipartFile blogThumbnail) {
        User user = userRepository.findById(blogRequest.getUserId()).orElseThrow(() -> new UsernameNotFoundException("Mã người dùng không tồn tại"));
        if (blogRepository.existsByTitle(blogRequest.getTitle())) {
            throw new ConflictException("Tên bài đăng này đã tồn tại");
        }
        Blog blog = modelMapper.map(blogRequest, Blog.class);
        blog.setUser(user);
        blog.setCreatedAt(Instant.now());

        String slug = GlobalUtil.convertToSlug(blogRequest.getTitle());
        if (blogRepository.existsBySlug(slug)) {
            throw new ConflictException("Vui lòng thay đổi tên bài đăng");
        }
        blog.setSlug(slug);
        String thumbnail = uploadFile.uploadFileOnCloudinary(blogThumbnail);
        blog.setThumbnail(thumbnail);
        blog.setApproved(false);

        Blog savedBlog = blogRepository.save(blog);
        return convertToBlogResponse(savedBlog);
    }

    // Ok
    @Override
    public BlogResponse update(Integer blogId, BlogRequest blogRequest, MultipartFile blogThumbnail) {
        Blog blog = blogRepository.findByIdAndIsApprovedTrue(blogId).orElseThrow(() -> new NotFoundException("Mã bài đăng không tồn tại hoặc bài đăng chưa được duyệt"));
        if (!Objects.equals(blogRequest.getUserId(), blog.getUser().getId())) {
            throw new AccessDeniedException("Chức năng này chỉ dành cho tác giả bài viết");
        }

        String slug = GlobalUtil.convertToSlug(blogRequest.getTitle());

        Blog checkDuplicatedBlog = blogRepository.findByTitleOrSlug(blogRequest.getTitle(), slug);
        if (checkDuplicatedBlog != null) {
            if (!(Objects.equals(blog.getId(), checkDuplicatedBlog.getId()))) {
                throw new ConflictException("Tên bài đăng hoặc slug đã tồn tại trước đây");
            }
        }

        blog.setSlug(slug);
        if (blogThumbnail != null) {
            uploadFile.deleteImageInCloudinary(blog.getThumbnail());
            String thumbnail = uploadFile.uploadFileOnCloudinary(blogThumbnail);
            blog.setThumbnail(thumbnail);
        }
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setDescription(blogRequest.getDescription());

        Blog savedBlog = blogRepository.save(blog);
        return convertToBlogResponse(savedBlog);
    }

    // Ok
    @Override
    public String switchApproved(Integer blogId, boolean isApproved) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Mã bài đăng không tồn tại"));
        blogRepository.switchApproved(blog.getId(), isApproved);
        return "Bài đăng đã được chuyển đổi trạng thái phê duyệt";
    }

    // Ok
    @Override
    public String delete(Integer blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Mã bài đăng không tồn tại"));

        // Lấy người dùng hiện tại từ context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        boolean isAdmin = currentUser.getRole().getName().equals("ROLE_ADMIN");
        boolean isAuthor = blog.getUser().getEmail().equals(currentUserEmail);
        if (!isAdmin && !isAuthor) {
            throw new AccessDeniedException("Chỉ quản trị viên hoặc tác giả mới có quyền xóa bài đăng này");
        }
        blogRepository.delete(blog);
        return "SUCCESS";
    }

    // Ok
    @Override
    public Page<BlogResponse> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return blogRepository.search(keyword, pageable).map(this::convertToBlogResponse);
    }

    // Ok
    private BlogResponse convertToBlogResponse(Blog savedBlog) {
        BlogResponse blogResponse = modelMapper.map(savedBlog, BlogResponse.class);
        Instant now = Instant.now();
        blogResponse.setCreatedAtFormat(GlobalUtil.convertDurationToString(Duration.between(savedBlog.getCreatedAt(), now)));
        blogResponse.setUsername(savedBlog.getUser().getUsername());
        blogResponse.setUserAvatar(savedBlog.getUser().getPhoto());
        blogResponse.setCreatedAt(savedBlog.getCreatedAt().toString());
        blogResponse.setApproved(savedBlog.isApproved());

        return blogResponse;
    }
}