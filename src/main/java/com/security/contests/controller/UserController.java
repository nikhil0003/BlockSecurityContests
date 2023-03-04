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
import com.security.contests.repository.RoleRepository;
import com.security.contests.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

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
			Role roledb = roleRepository.findByname(role);
			if (roledb != null && roledb.getName() != null && roledb.getName().equals(role)) {
				role1 = roledb;
			} else {
				role1.setName(role);
			}
			Set<UserRole> userRoles = new HashSet<>();
			userRoles.add(new UserRole(user, role1));
			userService.createUser(user, userRoles);
			model.addAttribute("createdAccount", "true");
			return "createAccount";

		}

	}

}
