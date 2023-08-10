package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/home")
public class MainController {

    private final CredentialsService credentialsService;
    private final FileService fileService;

    @GetMapping
    public String homeView(Authentication authentication, Model model) {
        try {
            String userName = authentication.getName();
            model.addAttribute("files", fileService.fileList(userName));
            model.addAttribute("credentials", credentialsService.getAllCredentials(userName));
            return "home";
        } catch (Exception e) {
            model.addAttribute("result", "Error: " + e.getMessage());
            return "result";
        }
    }
}
