package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.constant.CommonStatus;
import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.RoleRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.UserService;
import com.example.bdhv_itclub.utils.UploadFileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final UploadFileUtil uploadFile;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, UploadFileUtil uploadFile, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.uploadFile = uploadFile;
        this.passwordEncoder = passwordEncoder;
    }

    // Ok
    @Override
    public UserResponse get(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Mã người dùng không tồn tại");
        }
        if (user.get().getStatus() != null && user.get().getStatus().equals(CommonStatus.DELETED)) {
            throw new UsernameNotFoundException("Người dùng đã bị xoá");
        }
        return modelMapper.map(user.get(), UserResponse.class);
    }

    // Ok
    @Override
    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        if (user.getStatus() == CommonStatus.DELETED) {
            throw new UsernameNotFoundException("Người dùng đã bị xoá");
        }
        return modelMapper.map(user, UserResponse.class);
    }

    // Ok
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
    }

    // Ok
    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            return modelMapper.map(user, UserResponse.class);
        }).toList();
    }

    // Ok
    @Override
    public UserResponse createUser(UserRequest userRequest, MultipartFile img) {
        Role role = roleRepository.findById(userRequest.getRole().getId()).orElseThrow(() -> new NotFoundException("Mã vai trò không tồn tại"));
        User user = checkValid(userRequest, role, img);
        User savedUser = userRepository.save(user);
        return convertToUserResponse(savedUser);
    }

    // Ok
    @Override
    public UserResponse updateUser(UserRequest userRequest, Integer userId, MultipartFile img) {
        Role role = roleRepository.findById(userRequest.getRole().getId()).orElseThrow(() -> new NotFoundException("Mã vai trò không tồn tại"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Mã người dùng không tồn tại"));
        if (user.getStatus() != null && user.getStatus().equals(CommonStatus.DELETED)) {
            throw new UsernameNotFoundException("Người dùng đã bị xoá");
        }
        if (img != null) {
            if (user.getPhoto() != null) {
                uploadFile.deleteImageInCloudinary(user.getPhoto());
            }
            String url = uploadFile.uploadFileOnCloudinary(img);
            user.setPhoto(url);
        }
        if (!userRequest.getPassword().equals("Unknown password")) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        user.setRole(role);
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());

        User savedUser = userRepository.save(user);
        return convertToUserResponse(savedUser);
    }

    // Ok
    @Override
    public UserResponse updateUserInformation(String fullName, MultipartFile img, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        if (user.getStatus() != null && user.getStatus().equals(CommonStatus.DELETED)) {
            throw new UsernameNotFoundException("Người dùng đã bị xoá");
        }
        if (fullName != null) {
            user.setFullName(fullName);
        }
        if (img != null) {
            String urlImage = uploadFile.uploadFileOnCloudinary(img);
            user.setPhoto(urlImage);
        }
        User savedUser = userRepository.save(user);
        UserResponse userResponse = modelMapper.map(savedUser, UserResponse.class);
        return userResponse;
    }

    // Ok
    @Override
    public String changePassword(String password, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        if (user.getStatus() != null && user.getStatus().equals(CommonStatus.DELETED)) {
            throw new UsernameNotFoundException("Người dùng đã bị xoá");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "Thay đổi mật khẩu thành công";
    }

    // Ok
    @Override
    public void updateUserRefreshToken(String refreshToken, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email không tồn " + "tại"));
        if (user.getStatus() != null && user.getStatus().equals(CommonStatus.DELETED)) {
            throw new UsernameNotFoundException("Người dùng đã bị xoá");
        }
        userRepository.save(user);
    }

    // Ok
    @Override
    public String switchBlockStatus(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Mã người dùng không tồn tại"));
        if (user.getStatus() != null && user.getStatus().equals(CommonStatus.BLOCKED)) {
            user.setStatus(null);
        } else {
            user.setStatus(CommonStatus.BLOCKED);
        }
        userRepository.save(user);
        return "Cập nhật trạng thái thành công";
    }

    // Ok
    @Override
    public String delete(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Mã người dùng không tồn tại"));
        // Xoá ảnh trong cloudinary
        if (user.getPhoto() != null) {
            uploadFile.deleteImageInCloudinary(user.getPhoto());
        }
        userRepository.deleteById(user.getId());
        return "Xóa tài khoản thành công";
    }

    // Ok
    private UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return userResponse;
    }

    // Ok
    private User checkValid(UserRequest userRequest, Role role, MultipartFile img) {
        User user = modelMapper.map(userRequest, User.class);
        if (img != null) {
            String url = uploadFile.uploadFileOnCloudinary(img);
            user.setPhoto(url);
        }
        user.setEnabled(true);
        user.setCreatedTime(Instant.now());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }
}