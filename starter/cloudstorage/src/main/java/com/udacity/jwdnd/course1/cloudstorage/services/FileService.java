package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.FileUploadException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.FileNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public List<File> fileList(String userName) {
        return fileMapper.getFilesByUserId(userMapper.getUser(userName).getUserId());
    }

    public void uploadFile(MultipartFile multipartFile, String userName) throws FileUploadException {
        try {
            Integer userId = userMapper.getUser(userName).getUserId();
            String fileName = getCleanedFileName(multipartFile);

            //Checking if file size exceeds 10MB (as size limit)
            if (multipartFile.getSize() > 10 * 1024 * 1024) {
                throw new FileUploadException("File size exceeds the limit of 10MB.");
            }

            File file = createFileObject(fileName, multipartFile, userId);
            fileMapper.insertFile(file);
        } catch (NullPointerException e) {
            throw new FileUploadException("Error uploading the document.");
        }
    }
    
    public boolean fileAvailable(MultipartFile multipartFile, String name) {
        String uploadedFileName = multipartFile.getOriginalFilename();
        return fileList(name).stream().anyMatch(e -> e.getFilename().equalsIgnoreCase(uploadedFileName));
    }
    
    public void deleteFile(Integer fileid) {
        fileMapper.deleteFile(fileid);
    }
    
    public File getFile(Integer fileId) {
        File file = fileMapper.getFileById(fileId);
        if (file == null) {
            throw new FileNotFoundException("File not found with ID: " + fileId);
        }
        return file;
    }
    
    public String getCleanedFileName(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        return StringUtils.cleanPath(Objects.requireNonNull(originalFileName, "File name may not be null."));
    }

    public File createFileObject(String fileName, MultipartFile multipartFile, Integer userId) {
        try {
            return new File(null, fileName, multipartFile.getContentType(), multipartFile.getSize(), userId, multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
