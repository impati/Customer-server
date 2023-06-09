package com.example.customerserver.web.controller;

import com.example.customerserver.web.request.CodeRequest;
import com.example.customerserver.web.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/code")
    public String redirectWithCode(@CookieValue("redirectUrl") String redirectUrl, CodeRequest codeRequest, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("code", codeRequest.code());
        return "redirect:" + redirectUrl;
    }

    @RequestMapping(path = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(@RequestParam(defaultValue = "false") boolean error, Model model) {
        model.addAttribute("error", error);
        return "signin";
    }

}
