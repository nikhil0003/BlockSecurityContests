package com.security.contests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.contests.config.MyDataBaseConfiguration;
import com.security.contests.domain.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContestController {

	@Autowired
	private MyDataBaseConfiguration myDataBaseConfiguration;

	@GetMapping("/home")
	public String test(HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& user.getUserRoles().getRole().getName().equals("admin")) {
			model.addAttribute("isAdmin", true);
		}
		return "home";
	}

	@PostMapping(value = "/refreshData")
	public String referData(HttpServletRequest request, Model model) throws Exception {
		myDataBaseConfiguration.createDefaultDB();
		model.addAttribute("isAdmin", true);
		model.addAttribute("refresh", true);

		return "home";
	}
}
