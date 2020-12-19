package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {
//    @Value("${img.path}")
//    private String imgPath;

    private String createFilename(MultipartFile file) {
        if (file == null) {
            throw new MyException(ExceptionCode.SERVICE, "CREATE FILENAME - FILE IS NULL");
        }

        String originalFilename = file.getOriginalFilename();
        String[] arr = originalFilename.split("\\.");
        String extension = arr[arr.length - 1];

        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
        return filename + "." + extension;
    }

    public String addFile(MultipartFile file) {
        try {
            if (file == null || file.getBytes().length == 0) {
                return "";
            }
            String filename = createFilename(file);
//            String fullPath = imgPath + filename;
            String fullPath = filename;
            FileCopyUtils.copy(file.getBytes(), new File(fullPath));
            return filename;
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD FILE EXCEPTION");
        }
    }

    public String updateFile(MultipartFile file, String filename) {
        try {
            if (filename == null) {
                return "";
            }
            if (file == null || file.getBytes().length == 0) {
                return filename;
            }

//            String fullPath = imgPath + filename;
            String fullPath = filename;
            FileCopyUtils.copy(file.getBytes(), new File(fullPath));
            return filename;

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATE FILE EXCEPTION");
        }
    }

    public String removeFile(String filename) {
        if (filename == null) {
            throw new MyException(ExceptionCode.SERVICE, "REMOVE FILE - FILENAME IS NULL");
        }
//        String fullPath = imgPath + filename;
        String fullPath = filename;
        new File(fullPath).delete();
        return filename;
    }
}
