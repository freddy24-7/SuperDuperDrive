package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.CredentialsControllerException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping
public class CredentialsController {

    private final CredentialsService credentialsService;

    @PostMapping("/store")
    public String addCredential(Authentication authentication, @ModelAttribute Credentials credentials, Model model) {
        try {
            if (credentials.getCredentialId() == null) {
                credentialsService.InsertCredentials(credentials, authentication.getName());
            } else {
                credentialsService.updateCredentials(credentials, authentication.getName());
            }
            setSuccessModelAttributes(model, authentication.getName());
        } catch (CredentialsControllerException e) {
            setErrorModelAttributes(model, e.getMessage());
        }
        return "result";
    }

    @GetMapping("/{credentialId}")
    public String deleteCredential(Authentication authentication, Model model, @PathVariable Integer credentialId) {
        try {
            credentialsService.deleteCredential(credentialId);
            setSuccessModelAttributes(model, authentication.getName());
        } catch (CredentialsControllerException e) {
            setErrorModelAttributes(model, e.getMessage());
        }
            return "result";
        }

    @GetMapping("/home")
    public String displayHomePage(Authentication authentication, Model model) {
        try {
            String userName = authentication.getName();

            //Getting the list of credentials (encrypted)
            List<Credentials> credentialsList = credentialsService.getAllCredentials(userName);
            model.addAttribute("credentials", credentialsList);

        } catch (CredentialsControllerException e) {
            setErrorModelAttributes(model, e.getMessage());
            return "result";
        }
        return "home";
    }

    public void setErrorModelAttributes(Model model, String errorMessage) {
        model.addAttribute("result", "Error");
        model.addAttribute("errorMessage", errorMessage);
    }

    public void setSuccessModelAttributes(Model model, String userName) {
        model.addAttribute("result", "success");
        model.addAttribute("credentials", credentialsService.getAllCredentials(userName));
    }

}
