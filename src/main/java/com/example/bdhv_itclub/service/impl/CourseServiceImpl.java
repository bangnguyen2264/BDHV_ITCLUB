package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.*;
import com.example.bdhv_itclub.dto.request.CourseInfoRequest;
import com.example.bdhv_itclub.dto.request.CoursesRequest;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CategoryRepository;
import com.example.bdhv_itclub.repository.CoursesRepository;
import com.example.bdhv_itclub.repository.LessonRepository;
import com.example.bdhv_itclub.repository.VideoRepository;
import com.example.bdhv_itclub.service.CourseService;
import com.example.bdhv_itclub.utils.UploadFile;
import com.example.bdhv_itclub.utils.Utils;
import org.modelmapper.ModelMapper;
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
    private final UploadFile uploadFile;
    private final CoursesRepository coursesRepository;
    private final CategoryRepository categoryRepository;
    private final LessonRepository lessonRepository;
    private final VideoRepository videoRepository;

    public CourseServiceImpl(ModelMapper modelMapper, UploadFile uploadFile, CoursesRepository coursesRepository, CategoryRepository categoryRepository, LessonRepository lessonRepository, VideoRepository videoRepository) {
        this.modelMapper = modelMapper;
        this.uploadFile = uploadFile;
        this.coursesRepository = coursesRepository;
        this.categoryRepository = categoryRepository;
        this.lessonRepository = lessonRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public List<CourseReturnHomePageResponse> getCourseIntoHomePage(Integer categoryId) {
        List<Courses> listCourses = null;

        if (categoryId == null) {
            listCourses = coursesRepository.findAll();
        } else {
            // Kiểm tra nếu categoryId không tồn tại thì báo lỗi
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Course ID: " + categoryId + " không tồn tại"));

            listCourses = coursesRepository.findAllByCategoryId(categoryId);
        }

        // Map qua từng phần tử của listCourses, formatted lại dữ liệu
        return listCourses.stream().map(courses -> {
            // Mapper dữ liêu course qua dữ liệu muốn trả về
            CourseReturnHomePageResponse response = modelMapper.map(courses, CourseReturnHomePageResponse.class);
            // Tính toán các số liệu
            int totalReview = courses.getListReviews().size();
            int totalRating = courses.getListReviews().stream().mapToInt(Review::getRating).sum();
            double averageRating = (double) totalRating / totalReview;
            averageRating = Math.round(averageRating * 10.0) / 10.0;
            response.setTotalReview(totalReview);
            response.setAverageReview(averageRating);
            return response;
        }).toList();
    }

    @Override
    public CourseReturnDetailPageResponse getCourseDetail(String slug) {
        Courses course = coursesRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Course slug không tồn tại"));

        CourseReturnDetailPageResponse response = modelMapper.map(course, CourseReturnDetailPageResponse.class);
        sortChapterAndLesson(response);
        return response;
    }

    @Override
    public List<CourseReturnSearch> listAllCourseByKeyword(String keyword) {
        List<Courses> listCourses = coursesRepository.search(keyword);

        return listCourses.stream().map(courses -> {
            CourseReturnSearch response = modelMapper.map(courses, CourseReturnSearch.class);
            int totalReview = courses.getListReviews().size();
            int totalRating = courses.getListReviews().stream().mapToInt(Review::getRating).sum();
            double averageRating = (double) totalRating / totalReview;
            averageRating = Math.round(averageRating * 10.0) / 10.0;
            response.setAverageReview(averageRating);
            return response;
        }).toList();
    }

    @Override
    public List<CourseResponse> getAll() {
        List<Courses> coursesList = coursesRepository.findAll();
        return coursesList.stream().map(this::convertToCourseResponse).toList();
    }

    @Override
    public String updateIsEnabled(Integer courseId, boolean isEnabled) {
        Courses courses = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Courses ID không tồn tại"));

        coursesRepository.switchEnabled(courseId, isEnabled);

        return "SUCCESS";
    }

    @Override
    public String updateIsPublished(Integer courseId, boolean isPublished) {
        Courses courses = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Courses ID không tồn tại"));

        coursesRepository.switchPublished(courseId, isPublished);
        return "SUCCESS";
    }

    @Override
    public String updateIsFinished(Integer courseId, boolean isFinished) {
        Courses courses = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Courses ID không tồn tại"));

        coursesRepository.switchFinished(courseId, isFinished);
        return "SUCCESS";
    }

    @Override
    public CourseResponse create(CoursesRequest coursesRequest, MultipartFile image) {
        if (coursesRepository.existsCoursesByTitle(coursesRequest.getTitle())) {
            throw new ConflictException("Tên khóa học đã tồn tại!");
        }

        if (coursesRepository.existsCoursesBySlug(coursesRequest.getSlug())) {
            throw new ConflictException("Slug khóa học đã tồn tại!");
        }

        Category category = categoryRepository.findById(coursesRequest.getCategoryId()).orElseThrow(() -> new NotFoundException("Category ID không tồn tại"));

        Courses courses = new Courses();

        convertSomeAttributeToEntity(courses, coursesRequest);


        String thumbnail = uploadFile.uploadFileOnCloudinary(image);
        courses.setThumbnail(thumbnail);


        courses.setCategory(category);

        for (CourseInfoRequest request : coursesRequest.getInfoList()) {
            courses.addInfoList(request.getValue(), InformationType.valueOf(request.getType()));
        }

        Courses savedCourse = coursesRepository.save(courses);

        return modelMapper.map(savedCourse, CourseResponse.class);
    }

    @Override
    public CourseResponse get(Integer courseId) {
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Category ID không tồn tại"));

        CourseResponse response = modelMapper.map(course, CourseResponse.class);
        sortChapterAndLesson(response);
        return response;
    }

    @Override
    public CourseResponse update(Integer courseId, CoursesRequest coursesRequest, MultipartFile img) {
        Courses courseInDB = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course ID không tồn tại"));

        Category categoryInDB = categoryRepository.findById(coursesRequest.getCategoryId()).orElseThrow(() -> new NotFoundException("Category ID không tồn tại"));

        Courses courses = coursesRepository.findByTitleOrSlug(coursesRequest.getTitle(), coursesRequest.getSlug());

        if (courses != null) {
            if (!Objects.equals(courses.getId(), courseInDB.getId())) {
                throw new ConflictException("Tên/Slug khóa học đã tồn tại trước đó");
            }
        }

        if (img != null) {
            uploadFile.deleteImageInCloudinary(courseInDB.getThumbnail());
            String url = uploadFile.uploadFileOnCloudinary(img);
            courseInDB.setThumbnail(url);
        }

        convertSomeAttributeToEntity(courseInDB, coursesRequest);

        courseInDB.setCategory(categoryInDB);

        List<CourseInfo> infoList = new ArrayList<>();

        for (CourseInfoRequest request : coursesRequest.getInfoList()) {
            CourseInfo info = null;
            if (request.getId() != null) {
                info = new CourseInfo(request.getId(), request.getValue(), InformationType.valueOf(request.getType()), courseInDB);
            } else {
                info = new CourseInfo(request.getValue(), InformationType.valueOf(request.getType()), courseInDB);
            }
            infoList.add(info);
        }

        courseInDB.setInfoList(infoList);

        Courses savedCourse = coursesRepository.save(courseInDB);

        return modelMapper.map(savedCourse, CourseResponse.class);
    }

    private CourseResponse convertToCourseResponse(Courses course) {
        CourseResponse courseResponse = modelMapper.map(course, CourseResponse.class);
        int totalReview = course.getListReviews().size();
        int totalRating = course.getListReviews().stream().mapToInt(Review::getRating).sum();
        double averageRating = (double) totalRating / totalReview;
        averageRating = Math.round(averageRating * 10.0) / 10.0;
        courseResponse.setTotalReview(totalReview);
        courseResponse.setAverageReview(averageRating);
        courseResponse.setChapterList(null);
        courseResponse.setInfoList(null);
        return courseResponse;
    }

    private void convertSomeAttributeToEntity(Courses courses, CoursesRequest request) {
        courses.setTitle(request.getTitle());
        String slug = Utils.removeVietnameseAccents(request.getTitle());
        courses.setSlug(slug);
        courses.setDescription(request.getDescription());
        courses.setPrice(request.getPrice());
        courses.setDiscount(request.getDiscount());
        courses.setEnabled(request.isEnabled());
        courses.setPublished(request.isPublished());
        courses.setFinished(request.isFinished());
        if (request.isPublished()) {
            courses.setPublishedAt(Instant.now());
        }
    }

    private void sortChapterAndLesson(CourseResponse response) {
        int totalLessonInCourse = 0;
        List<ChapterDTO> chapterList = response.getChapterList();
        response.setTotalChapter(chapterList.size());
        chapterList.sort(Comparator.comparingInt(ChapterDTO::getOrders));
        for (ChapterDTO chapterDTO : chapterList) {
            List<LessonResponse> listLesson = chapterDTO.getLessonList();
            listLesson.sort(Comparator.comparingInt(LessonResponse::getOrders));
            totalLessonInCourse += listLesson.size();
            chapterDTO.setTotalLesson(listLesson.size());
        }
        response.setTotalLesson(totalLessonInCourse);
    }

    // Hàm sắp xếp lại chapter và lesson theo order
    private void sortChapterAndLesson(CourseReturnDetailPageResponse response) {
        int totalLessonInCourse = 0;
        Duration duration = Duration.ZERO;

        // Lấy ra chapters của course (theo DTO)
        List<ChapterReturnDetailResponse> chapterList = response.getChapterList().stream().map(chapter -> modelMapper.map(chapter, ChapterReturnDetailResponse.class)).collect(Collectors.toList());
        // Set số lượng chapter
        response.setTotalChapter(chapterList.size());
        // sort lại chapter theo order
        chapterList.sort(Comparator.comparingInt(ChapterReturnDetailResponse::getOrders));
        int i = 1;
        for (ChapterReturnDetailResponse chapter : chapterList) {
            // Lây ra lessons của chapter đó
            List<LessonReturnDetailResponse> listLesson = chapter.getLessonList();
            // Sort lại lesson theo order
            listLesson.sort(Comparator.comparingInt(LessonReturnDetailResponse::getOrders));
            // Lặp lại lesson
            for (LessonReturnDetailResponse lesson : listLesson) {
                // Nếu mà type lesson đó là dạng video
                if (lesson.getLessonType().equals(LessonType.VIDEO)) {
                    Lesson lessonInDB = lessonRepository.findById(lesson.getId()).get();
                    Video video = videoRepository.findById(lessonInDB.getVideo().getId()).get();
                    // Gán thời lượng là = thời lượng video
                    lesson.setDuration(video.getDuration());

                    // Tính toán thời lượng video
                    duration = duration.plus(Duration.ofMinutes(video.getDuration().getMinute()).plusSeconds(video.getDuration().getSecond()));
                } else {
                    // Nếu type lesson không phải là dạng video thì mặc định sẽ gán duration là 1 phút
                    LocalTime time = LocalTime.of(0, 1, 0);
                    lesson.setDuration(time);
                    duration = duration.plus(Duration.ofMinutes(1));
                }
                lesson.setOrders(i);
                ++i;
            }
            // Gán tổng số bài học của 1 chapter
            totalLessonInCourse += listLesson.size();
            chapter.setTotalLesson(listLesson.size());
        }

        long hours = duration.toHours(); // Lấy tổng giờ
        long minutes = duration.toMinutes(); // Lấy tổng số phút
        long seconds = duration.minusMinutes(minutes).getSeconds(); // Lấy số giây còn lại sau khi lấy tổng số phút

        // Tạo LocalTime từ số phút và số giây
        LocalTime localTime = LocalTime.of((int) hours, (int) minutes, (int) seconds);
        response.setTotalTime(localTime);
        response.setTotalLesson(totalLessonInCourse);
    }
}
