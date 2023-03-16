package com.example.customerserver.web.controller;

import com.example.customerserver.web.SignupManager;
import com.example.customerserver.web.request.CodeRequest;
import com.example.customerserver.web.request.SignupRequest;
import com.example.customerserver.web.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupManager signupManager;

    @ResponseBody
    @PostMapping("/gettoken")
    public TokenResponse returnToken(TokenResponse tokenResponse) {
        return tokenResponse;
    }

    @PostMapping("/code")
    public String redirectWithCode(@CookieValue("redirectUrl") String redirectUrl,
                                   CodeRequest codeRequest,
                                   RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("code", codeRequest.code());
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/login")
    public String login(@RequestParam String redirectUrl,
                        HttpServletResponse response,
                        Model model) {
        response.addCookie(new Cookie("redirectUrl", redirectUrl));
        model.addAttribute("error", false); // TODO : keycloak 로그인이 실패할 경우 대응
        return "signin";
    }

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

        return "redirect:/auth/login";
    }


}
