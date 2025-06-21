package com.example.bdhv_itclub.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Lớp tiện ích dùng để xử lý việc upload và xóa file trên Cloudinary.
 *
 * - Cung cấp phương thức upload file (ảnh, video hoặc file khác) lên Cloudinary.
 * - Cung cấp phương thức xóa ảnh hoặc video trên Cloudinary dựa theo URL.
 * - Xử lý ngoại lệ và ném về ngoại lệ tùy chỉnh với mã lỗi HTTP tương ứng.
 * - Có xử lý tránh xóa nhầm file mặc định (trong trường hợp ảnh).
 */
@Component
public class UploadFileUtil {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadFileOnCloudinary(MultipartFile file) {
        try {
            Map r = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            return (String) r.get("secure_url");
        } catch (IOException e) {
            throw new IllegalArgumentException("Tải file lên cloudinary không thành công");
        }
    }

    public void deleteImageInCloudinary(String url) {
        try {
            int lastSlashIndex = url.lastIndexOf('/');
            int lastDotIndex = url.lastIndexOf('.');
            String fileName = url.substring(lastSlashIndex + 1, lastDotIndex);
            if (!fileName.equals("ooozzfj7t7p1zokgonni")) {
                cloudinary.uploader().destroy(fileName, ObjectUtils.asMap("resource_type", "image"));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Xoá ảnh trên cloudinary thất bại");
        }
    }

    public void deleteVideoInCloudinary(String url) {
        try {
            int lastSlashIndex = url.lastIndexOf('/');
            int lastDotIndex = url.lastIndexOf('.');
            String fileName = url.substring(lastSlashIndex + 1, lastDotIndex);
            cloudinary.uploader().destroy(fileName, ObjectUtils.asMap("resource_type", "video"));
        } catch (IOException e) {
            throw new IllegalArgumentException("Xoá video trên cloudinary thất bại");
        }
    }
}