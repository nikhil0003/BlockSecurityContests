package com.security.contests.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.contests.config.SecurityUtility;
import com.security.contests.config.UserSecurityService;
import com.security.contests.domain.Role;
import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;
import com.security.contests.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@Autowired
	private UserSecurityService userSecurityService;

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
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username, @ModelAttribute("password") String password, Model model)
			throws Exception {
		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			return "createAccount";
		}

		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			return "createAccount";
		}

		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);
		model.addAttribute("createdAccount", "true");
		return "createAccount";
	}

	
}
