package com.security.contests.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.security.contests.domain.Contest;
import com.security.contests.domain.Contestant;
import com.security.contests.domain.CreateConstestModel;
import com.security.contests.domain.Judge;
import com.security.contests.domain.JudgeDisplay;
import com.security.contests.domain.Sponser;
import com.security.contests.domain.User;
import com.security.contests.domain.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.contests.config.MyDataBaseConfiguration;
import com.security.contests.repository.CustomDAO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContestController {


	private static final Long JUDGE_TOTAL_SHARE_PERCENTAGE = 20L;
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
	
	@GetMapping("/contest/{id}")
	public String getContestPage(Model model,@PathVariable Long id) {
		Contest c = customDAO.findByContestId(id);
		CreateConstestModel contest = new CreateConstestModel();
		if(c !=null) {
			contest.setStartDate(c.getStartDate());
			contest.setFirstname(c.getName());
			contest.setEndDate(c.getEndDate());
		}
		model.addAttribute("contest", contest);
		return "contestpage";
	}

	@PostMapping(value = "/refreshData")
	public String referData(HttpServletRequest request, Model model) throws Exception {
		myDataBaseConfiguration.createDefaultDB();
		model.addAttribute("isAdmin", true);
		model.addAttribute("refresh", true);

		return "home";
	}

	@PostMapping(value ="/getGradeSubmissionForm")
	public String getGradeSubmissionForm(@ModelAttribute("contestant") Contestant contestant, HttpServletRequest request, Model model) {
		model.addAttribute("contestant", contestant);
		return "gradeSubmissionForm";
	}

	@PostMapping(value ="/gradeSubmission")
	public String gradeSubmission(@ModelAttribute("contestant") Contestant contestant, HttpServletRequest request, Model model) {
		customDAO.updateContestantGrade(contestant.getId(), contestant.getGrade());
		return "contestList"; //TODO: Replace with contestantList to grade another contestant
	}

	@GetMapping(value ="/distributeRewards")
	public String distributeRewards(@ModelAttribute("contest") Contest contest, HttpServletRequest request, Model model) {
		final Long sponserAmount = contest.getSponserAmount();
		List<Judge> judges = contest.getJudgeList();
		List<Contestant> contestants = contest.getContestant();
		final Long totalRewardForJudges = (JUDGE_TOTAL_SHARE_PERCENTAGE * sponserAmount) / 100;
		final Long rewardPerJudge = totalRewardForJudges / judges.size();
		final Long totalRewardForContestants = sponserAmount - totalRewardForJudges;
		final Long rewardForWinner = (70 * totalRewardForContestants) / 100;
		final Long rewardForRunner = totalRewardForContestants - rewardForWinner;

		judges.stream()
				.forEach(judge -> {
					customDAO.updateWalletData(judge.getUser().getId(), rewardPerJudge);
					Wallet wallet = customDAO.findWalletByUserId(judge.getUser().getId());
					customDAO.saveLedgerData(wallet.getId(), rewardPerJudge, "Contest Fee");
				});
		Collections.sort(contestants, Comparator.comparing(Contestant::getGrade));
		Collections.sort(contestants, Collections.reverseOrder());
		Contestant winner = contestants.get(0);
		Contestant runner = contestants.get(1);

		Wallet wallet1 = customDAO.findWalletByUserId(winner.getUser().getId());
		customDAO.updateWalletData(winner.getUser().getId(), wallet1.getBalance() + rewardForWinner);
		customDAO.saveLedgerData(wallet1.getId(), rewardForWinner, "Contest Winner");

		Wallet wallet2 = customDAO.findWalletByUserId(runner.getUser().getId());
		customDAO.updateWalletData(runner.getUser().getId(), wallet2.getBalance() + rewardForRunner);
		customDAO.saveLedgerData(wallet2.getId(), rewardForRunner, "Contest Runner");

		Sponser sponser = contest.getSponser();
		Wallet wallet3 = customDAO.findWalletByUserId(sponser.getUser().getId());
		final Long balanceAfterDistribution = wallet3.getBalance() - sponserAmount;
		customDAO.updateWalletData(sponser.getUser().getId(), balanceAfterDistribution);
		customDAO.saveLedgerData(wallet1.getId(), -sponserAmount, "Contest Sponsership amount");

		return "contestList";
	}

}
