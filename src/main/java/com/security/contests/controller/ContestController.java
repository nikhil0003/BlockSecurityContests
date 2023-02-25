package com.security.contests.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.contests.domain.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContestController {

	@GetMapping("/home")
	public String test(HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {
		user.getUserRoles().forEach(i -> {
			if (i.getRole().getName().equals("admin")) {
				model.addAttribute("isAdmin", true);
			}
		});
		return "home";
	}

	@PostMapping(value = "/refreshData")
	public String referData(HttpServletRequest request, Model model) throws Exception {
		
		model.addAttribute("isAdmin", true);
		return "home";
	}
}
