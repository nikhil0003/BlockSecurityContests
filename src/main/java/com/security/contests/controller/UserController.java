package com.security.contests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.contests.config.SecurityUtility;
import com.security.contests.domain.Role;
import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;
import com.security.contests.repository.RoleRepository;
import com.security.contests.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/createAccount")
	public String createAccount() {
		return "createAccount";
	}

	@GetMapping("/forgetPassword")
	public String forgetPassword() {
		return "forgetpassword";
	}

	@GetMapping("/badRequest")
	public String badRequest() {
		return "badRequestPage";
	}

	@PostMapping(value = "/createAccount")
	public String newUserPost(HttpServletRequest request, @ModelAttribute("username") String username,
			@ModelAttribute("password") String password, @ModelAttribute("role") String role,
			@ModelAttribute("confirmpassword") String confirmpassword, Model model) throws Exception {
		if (!(password != null && username != null)) {
			model.addAttribute("enterDetails", true);
			return "createAccount";
		} else {
			if (!password.equals(confirmpassword)) {
				model.addAttribute("passwordMismatch", true);
				return "createAccount";
			}
			if (userService.findByUsername(username) != null) {
				model.addAttribute("usernameExists", true);
				return "createAccount";
			}

			User user = new User();
			user.setUsername(username);
			String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
			user.setPassword(encryptedPassword);
			Role role1 = new Role();
			role1.setName(role);
			UserRole ur = new UserRole(user, role1);
			user.setUserRoles(ur);
			userService.createUser(user, ur);
			model.addAttribute("createdAccount", "true");
			return "createAccount";

		}

	}

}
