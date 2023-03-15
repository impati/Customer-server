package com.example.customerserver.web.controller;

import com.example.customerserver.web.request.CodeRequest;
import com.example.customerserver.web.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

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

}
