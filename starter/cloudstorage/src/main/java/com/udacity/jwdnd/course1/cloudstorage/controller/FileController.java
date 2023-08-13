package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.EmptyFileException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.FileAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.FileUploadException;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Controller
@RequestMapping()
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file, Model model) {
        try {
            if (file.isEmpty()) {
                throw new EmptyFileException("You are trying to upload an empty file");
            }
            if (fileService.fileAvailable(file, authentication.getName())) {
                throw new FileAlreadyExistsException("File already exists with the same name.");
            }
            if (file.getSize() > 1024 * 1024) {
                throw new FileUploadException("File size exceeds 1 MB");
            }
            fileService.uploadFile(file, authentication.getName());
            model.addAttribute("result", "success");
            model.addAttribute("files", fileService.fileList(authentication.getName()));
        } catch (EmptyFileException | FileAlreadyExistsException | FileUploadException e) {
            model.addAttribute("result", "Error: " + e.getMessage());
        }
        return "result";
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(Authentication authentication, Model model, @PathVariable Integer fileId) {
        try {
            fileService.deleteFile(fileId);
            model.addAttribute("result", "success");
        } catch (Exception e) {
            model.addAttribute("result", "Error");
        }
        model.addAttribute("files", fileService.fileList(authentication.getName()));
        return "result";
    }

    @GetMapping("/download/{fileId}")
    @ResponseBody
    public byte[] downloadFile(@PathVariable Integer fileId, Model model) {
        try {
            model.addAttribute("result", "success");
            return fileService.getFile(fileId).getFiledata();
        } catch (Exception e) {
            model.addAttribute("result", "Error");
            return fileService.getFile(fileId).getFiledata();
        }
    }
}

