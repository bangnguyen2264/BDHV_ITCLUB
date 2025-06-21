package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.constant.CourseInformationType;
import com.example.bdhv_itclub.constant.LessonType;
import com.example.bdhv_itclub.dto.reponse.*;
import com.example.bdhv_itclub.dto.request.CourseChapterDTO;
import com.example.bdhv_itclub.dto.request.CourseInformationRequest;
import com.example.bdhv_itclub.dto.request.CourseRequest;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CourseCategoryRepository;
import com.example.bdhv_itclub.repository.CourseRepository;
import com.example.bdhv_itclub.repository.LessonRepository;
import com.example.bdhv_itclub.repository.VideoRepository;
import com.example.bdhv_itclub.service.CourseService;
import com.example.bdhv_itclub.utils.GlobalUtil;
import com.example.bdhv_itclub.utils.UploadFileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class CourseServiceImpl implements CourseService {
    private final ModelMapper modelMapper;
    private final UploadFileUtil uploadFile;
    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final LessonRepository lessonRepository;
    private final VideoRepository videoRepository;

    public CourseServiceImpl(ModelMapper modelMapper, UploadFileUtil uploadFile, CourseRepository courseRepository, CourseCategoryRepository courseCategoryRepository, LessonRepository lessonRepository, VideoRepository videoRepository) {
        this.modelMapper = modelMapper;
        this.uploadFile = uploadFile;
        this.courseRepository = courseRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.lessonRepository = lessonRepository;
        this.videoRepository = videoRepository;
    }

    // Ok
    @Override
    public CourseResponse get(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        CourseResponse courseResponse = modelMapper.map(course, CourseResponse.class);
        sortChapterAndLesson(courseResponse);
        return courseResponse;
    }

    // Ok
    @Override
    public Page<CourseResponseForHomePage> getCoursesForHomePageAndByCategoryId(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursePage;
        if (categoryId == null) {
            coursePage = courseRepository.findEnabledExcludeDeletedCategory(pageable);
        } else {
            courseCategoryRepository.findActiveById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Mã danh mục khóa học không tồn tại hoặc ở trạng thái bị xóa"));
            coursePage = courseRepository.findEnabledByCategoryIdExcludeDeletedCategory(categoryId, pageable);
        }

        Page<CourseResponseForHomePage> mappedPage = coursePage.map(course -> {
            CourseResponseForHomePage response = modelMapper.map(course, CourseResponseForHomePage.class);
            int totalReview = course.getReviews().size();
            int totalRating = course.getReviews().stream().mapToInt(Review::getRating).sum();
            double averageRating = totalReview == 0 ? 0.0 : Math.round((double) totalRating / totalReview * 10.0) / 10.0;
            response.setTotalReview(totalReview);
            response.setAverageReview(averageRating);
            return response;
        });
        return mappedPage;
    }

    // Ok
    @Override
    public CourseResponseForDetailPage getCourseForDetailPage(String slug) {
        Course course = courseRepository.findEnabledAndCategoryActiveBySlug(slug).orElseThrow(() -> new NotFoundException("Slug của khóa học không tồn tại hoặc khóa học bị ẩn"));
        CourseResponseForDetailPage courseResponseForDetailPage = modelMapper.map(course, CourseResponseForDetailPage.class);
        sortChapterAndLesson(courseResponseForDetailPage);
        return courseResponseForDetailPage;
    }

    // Ok
    @Override
    public Page<CourseResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findAll(pageable).map(this::convertToCourseResponse);
    }

    // Ok
    @Override
    public Page<CourseResponseForSearching> getAllByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseRepository.search(keyword, pageable);
        return courses.map(course -> {
            CourseResponseForSearching courseResponseForSearching = modelMapper.map(course, CourseResponseForSearching.class);
            int totalReview = course.getReviews().size();
            int totalRating = course.getReviews().stream().mapToInt(Review::getRating).sum();
            double averageRating = (double) totalRating / totalReview;
            averageRating = Math.round(averageRating * 10.0) / 10.0;
            courseResponseForSearching.setAverageReview(averageRating);
            return courseResponseForSearching;
        });
    }

    // Ok
    @Override
    public CourseResponse create(CourseRequest courseRequest, MultipartFile courseThumbnail) {
        if (courseRepository.existsByTitle(courseRequest.getTitle())) {
            throw new ConflictException("Tên khóa học đã tồn tại");
        }
        if (courseRepository.existsBySlug(courseRequest.getSlug())) {
            throw new ConflictException("Slug khóa học đã tồn tại");
        }
        CourseCategory courseCategory = courseCategoryRepository.findActiveById(courseRequest.getCategoryId()).orElseThrow(() -> new NotFoundException("Mã danh mục khóa học không tồn tại hoặc ở trạng thái xóa"));
        Course course = new Course();
        convertSomeAttributeToEntity(course, courseRequest);

        String thumbnail = uploadFile.uploadFileOnCloudinary(courseThumbnail);
        course.setThumbnail(thumbnail);
        course.setCategory(courseCategory);
        for (CourseInformationRequest request : courseRequest.getCourseInformations()) {
            course.addCourseInformations(request.getValue(), CourseInformationType.valueOf(request.getType()));
        }
        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseResponse.class);
    }

    // Ok
    @Override
    public CourseResponse update(Integer courseId, CourseRequest courseRequest, MultipartFile courseThumbnail) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        CourseCategory courseCategory = courseCategoryRepository.findActiveById(courseRequest.getCategoryId()).orElseThrow(() -> new NotFoundException("Mã danh mục khóa học không tồn tại hoặc ở trạng thái bị xóa"));

        Course checkDuplicatedCourse = courseRepository.findByTitleOrSlug(courseRequest.getTitle(), courseRequest.getSlug());
        if (checkDuplicatedCourse != null) {
            if (!Objects.equals(checkDuplicatedCourse.getId(), course.getId())) {
                throw new ConflictException("Tên hoặc slug của khóa học đã tồn tại trước đó");
            }
        }
        if (courseThumbnail != null) {
            uploadFile.deleteImageInCloudinary(course.getThumbnail());
            String url = uploadFile.uploadFileOnCloudinary(courseThumbnail);
            course.setThumbnail(url);
        }
        convertSomeAttributeToEntity(course, courseRequest);
        course.setCategory(courseCategory);

        List<CourseInformation> courseInformations = new ArrayList<>();

        for (CourseInformationRequest request : courseRequest.getCourseInformations()) {
            CourseInformation courseInformation = null;
            if (request.getId() != null) {
                courseInformation = new CourseInformation(request.getId(), request.getValue(), CourseInformationType.valueOf(request.getType()), course);
            } else {
                courseInformation = new CourseInformation(request.getValue(), CourseInformationType.valueOf(request.getType()), course);
            }
            courseInformations.add(courseInformation);
        }
        course.setCourseInformations(courseInformations);
        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseResponse.class);
    }

    // Ok
    @Override
    public String updateIsEnabled(Integer courseId, boolean isEnabled) {
        courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        courseRepository.switchEnabled(courseId, isEnabled);
        return "Đổi trạng thái khóa học thành công";
    }

    // Ok
    @Override
    public String updateIsPublished(Integer courseId, boolean isPublished) {
        courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        courseRepository.switchPublished(courseId, isPublished);
        return "Đổi trạng thái xuất bản thành công";
    }

    // Ok
    @Override
    public String updateIsFinished(Integer courseId, boolean isFinished) {
        courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        courseRepository.switchFinished(courseId, isFinished);
        return "Đổi trạng thái kết thúc thành công";
    }

    // Ok
    private CourseResponse convertToCourseResponse(Course course) {
        CourseResponse courseResponse = modelMapper.map(course, CourseResponse.class);
        int totalReview = course.getReviews().size();
        int totalRating = course.getReviews().stream().mapToInt(Review::getRating).sum();
        double averageRating = totalReview == 0 ? 0.0 : Math.round((double) totalRating / totalReview * 10) / 10.0;
        courseResponse.setTotalReview(totalReview);
        courseResponse.setAverageReview(averageRating);
        courseResponse.setCourseChapters(null);
        courseResponse.setCourseInformations(null);
        return courseResponse;
    }

    // Ok
    private void convertSomeAttributeToEntity(Course course, CourseRequest courseRequest) {
        course.setTitle(courseRequest.getTitle());
        String slug = GlobalUtil.convertToSlug(courseRequest.getTitle());
        course.setSlug(slug);
        course.setDescription(courseRequest.getDescription());
        course.setPrice(courseRequest.getPrice());
        course.setDiscount(courseRequest.getDiscount());
        course.setEnabled(courseRequest.isEnabled());
        course.setPublished(courseRequest.isPublished());
        course.setFinished(courseRequest.isFinished());
        if (courseRequest.isPublished()) {
            course.setPublishedAt(Instant.now());
        }
    }

    // Ok
    private void sortChapterAndLesson(CourseResponse courseResponse) {
        int totalCourseLesson = 0;
        List<CourseChapterDTO> chapters = courseResponse.getCourseChapters();
        courseResponse.setTotalChapter(chapters.size());
        chapters.sort(Comparator.comparingInt(CourseChapterDTO::getChapterOrder));
        for (CourseChapterDTO chapterDTO : chapters) {
            List<LessonResponse> lessons = chapterDTO.getLessons();
            lessons.sort(Comparator.comparingInt(LessonResponse::getLessonOrder));
            totalCourseLesson += lessons.size();
            chapterDTO.setTotalLesson(lessons.size());
        }
        courseResponse.setTotalLesson(totalCourseLesson);
    }

    // Ok
    private void sortChapterAndLesson(CourseResponseForDetailPage courseResponseForDetailPage) {
        int totalCourseLesson = 0;
        Duration duration = Duration.ZERO;
        List<CourseChapterResponseForDetailPage> chapters = courseResponseForDetailPage.getCourseChapters().stream().map(chapter -> modelMapper.map(chapter, CourseChapterResponseForDetailPage.class)).collect(Collectors.toList());
        courseResponseForDetailPage.setTotalChapter(chapters.size());
        chapters.sort(Comparator.comparingInt(CourseChapterResponseForDetailPage::getChapterOrder));
        int i = 1;
        for (CourseChapterResponseForDetailPage chapter : chapters) {
            List<LessonResponseForDetailPage> lessons = chapter.getLessons();
            lessons.sort(Comparator.comparingInt(LessonResponseForDetailPage::getLessonOrder));
            for (LessonResponseForDetailPage lesson : lessons) {
                if (lesson.getLessonType().equals(LessonType.VIDEO)) {
                    Lesson lessonInDB = lessonRepository.findById(lesson.getId()).get();
                    Video video = videoRepository.findById(lessonInDB.getVideo().getId()).get();
                    lesson.setDuration(video.getDuration());
                    duration = duration.plus(Duration.ofMinutes(video.getDuration().getMinute()).plusSeconds(video.getDuration().getSecond()));
                } else {
                    LocalTime time = LocalTime.of(0, 1, 0);
                    lesson.setDuration(time);
                    duration = duration.plus(Duration.ofMinutes(1));
                }
                lesson.setLessonOrder(i);
                ++i;
            }
            totalCourseLesson += lessons.size();
            chapter.setTotalLesson(lessons.size());
        }

        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();

        LocalTime localTime = LocalTime.of((int) hours, (int) minutes, (int) seconds);
        courseResponseForDetailPage.setTotalTime(localTime);
        courseResponseForDetailPage.setTotalLesson(totalCourseLesson);
    }
}