package com.example.customerserver.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.customerserver.web.request.CodeRequest;
import com.example.customerserver.web.response.TokenResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@ResponseBody
	@PostMapping("/gettoken")
	public TokenResponse returnToken(final TokenResponse tokenResponse) {
		return tokenResponse;
	}

	@GetMapping("/code")
	public String redirectWithCode(
		@CookieValue("redirectUrl") final String redirectUrl,
		final CodeRequest codeRequest,
		final RedirectAttributes redirectAttributes
	) {
		redirectAttributes.addAttribute("code", codeRequest.code());

		return "redirect:" + redirectUrl;
	}

	@RequestMapping(path = "/login", method = {RequestMethod.GET, RequestMethod.POST})
	public String login(@RequestParam(defaultValue = "false") final boolean error, final Model model) {
		model.addAttribute("error", error);

		return "signin";
	}
}
