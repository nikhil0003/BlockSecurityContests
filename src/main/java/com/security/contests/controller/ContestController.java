package com.security.contests.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.contests.config.MyDataBaseConfiguration;
import com.security.contests.domain.Contest;
import com.security.contests.domain.CreateConstestModel;
import com.security.contests.domain.JudgeDisplay;
import com.security.contests.domain.User;
import com.security.contests.repository.CustomDAO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContestController {

	@Autowired
	private MyDataBaseConfiguration myDataBaseConfiguration;

	@Autowired
	private CustomDAO customDAO;

	@GetMapping("/")
	public String index(Model model) {
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String test(HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& user.getUserRoles().getRole().getName().equals("admin")) {
			model.addAttribute("isAdmin", true);
		}
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& user.getUserRoles().getRole().getName().equals("sponser")) {
			model.addAttribute("isSponser", true);
		}
		return "home";
	}

	@GetMapping("/createContest")
	public String createGetContest(HttpServletRequest request, Model model) {
		ArrayList<JudgeDisplay> jdlist = customDAO.listJudges();

		CreateConstestModel ccm = new CreateConstestModel();
		if (jdlist != null && !jdlist.isEmpty()) {
			ccm.setJdlist(jdlist);
		}
		ccm.setEndDate(new Date());
		ccm.setStartDate(new Date());
		model.addAttribute("ccm", ccm);
		return "createContest";
	}

	@PostMapping("/PostcreateContest")
	public String createContest(@ModelAttribute("ccm") CreateConstestModel ccm, Model model) {
		if (!ccm.getJdlist().isEmpty()) {
			long jds = ccm.getJdlist().stream().filter(i -> i.isJudgeSno()).count();
			if (jds > 5 && jds < 10) {
				model.addAttribute("noofjds", true);
			}
		}
		int saveContestJudge = 0;
		List<JudgeDisplay> selectedJudgesList = ccm.getJdlist().stream().filter(i -> i.isJudgeSno()).toList();
		Contest contestName = customDAO.findByContest(ccm.getFirstname());
		if (contestName == null) {
			int contestSave = customDAO.saveContestData(ccm);
			if (contestSave > 0) {
				contestName = customDAO.findByContest(ccm.getFirstname());
				saveContestJudge = customDAO.saveContestJudge(contestName, ccm);
			}

		} else {
			model.addAttribute("contestAlreadyFound", true);

		}

		if (saveContestJudge > 0) {
			return "redirect:/contestList";
		} else {
			return "createContest";
		}

	}

	@GetMapping("/contestList")
	public String getContestList(Model model) {
		ArrayList<Contest> contestList = customDAO.listContests();
		model.addAttribute("contestList", contestList);
		return "contestList";
	}

	@PostMapping(value = "/refreshData")
	public String referData(HttpServletRequest request, Model model) throws Exception {
		myDataBaseConfiguration.createDefaultDB();
		model.addAttribute("isAdmin", true);
		model.addAttribute("refresh", true);

		return "home";
	}
}
