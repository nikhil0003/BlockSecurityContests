package com.security.contests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.contests.domain.User;
import com.security.contests.repository.ContestRepositry;
import com.security.contests.repository.ContestantRepositry;
import com.security.contests.repository.JudgeRepositry;
import com.security.contests.repository.LedgerRepositry;
import com.security.contests.repository.RoleRepository;
import com.security.contests.repository.SponserRepositry;
import com.security.contests.repository.UserRepository;
import com.security.contests.repository.UserRoleRepositry;
import com.security.contests.repository.WalletRepositry;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContestController {

	@Autowired
	private ContestantRepositry contestantRepositry;

	@Autowired
	private ContestRepositry contestRepositry;

	@Autowired
	private JudgeRepositry JudgeRepositry;

	@Autowired
	private LedgerRepositry LedgerRepositry;

	@Autowired
	private RoleRepository RoleRepository;

	@Autowired
	private SponserRepositry SponserRepositry;

	@Autowired
	private UserRepository UserRepository;

	@Autowired
	private UserRoleRepositry UserRoleRepositry;

	@Autowired
	private WalletRepositry WalletRepositry;

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
//		WalletRepositry.deleteAll();
//		LedgerRepositry.deleteAll();
//		contestRepositry.deleteAll();
//		contestantRepositry.deleteAll();
//		JudgeRepositry.deleteAll();
//		SponserRepositry.deleteAll();
//		UserRepository.deleteAll();
//		UserRoleRepositry.deleteAll();
//		RoleRepository.deleteAll();
		
		
		model.addAttribute("isAdmin", true);
		model.addAttribute("refresh", true);

		return "home";
	}
}
