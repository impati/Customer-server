package com.example.customerserver.web.controller;

import com.example.customerserver.web.SignupManager;
import com.example.customerserver.web.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final SignupManager signupManager;

    @GetMapping("/signup")
    public String signup(@ModelAttribute SignupRequest signupRequest, Model model) {
        model.addAttribute("signupRequest", signupRequest);
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupRequest signupRequest, BindingResult bindingResult) {
        signupRequest.validPassword();
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        signupManager.signup(signupRequest);
        return "forward:/auth/login";
    }

}
