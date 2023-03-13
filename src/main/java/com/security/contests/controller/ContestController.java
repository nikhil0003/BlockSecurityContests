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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.contests.config.MyDataBaseConfiguration;
import com.security.contests.domain.Contest;
import com.security.contests.domain.Contestant;
import com.security.contests.domain.CreateConstestModel;
import com.security.contests.domain.JudgeDisplay;
import com.security.contests.domain.User;
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
	public String getContestPage(Model model, @PathVariable Long id, @AuthenticationPrincipal User user) {
		Contest contestData = customDAO.findByContestId(id);
		CreateConstestModel contest = new CreateConstestModel();
		if (contestData != null) {
			contest.setStartDate(contestData.getStartDate());
			contest.setFirstname(contestData.getName());
			contest.setEndDate(contestData.getEndDate());
			contest.setId(contestData.getId());
		}

		boolean isjoinConstent = false;
		boolean isShowdata = false;
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& user.getUserRoles().getRole().getName().equals("contestant")
				&& contestData.getEndDate().compareTo(new Date()) > 0) {
			isjoinConstent = true;

		}

		ArrayList<Contestant> participentsList = customDAO.listContestantForContest(contestData);
		if (!participentsList.isEmpty()
				&& participentsList.stream().anyMatch(i -> i.getUser().getId().equals(user.getId()))) {
			// already joined
			isjoinConstent = false;
			isShowdata = true;
		}

		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& (user.getUserRoles().getRole().getName().equals("contestant"))) {
			// show only contest in future for judge
			isShowdata = true;

		} else {
			isShowdata = false;
		}

		if (!participentsList.isEmpty() && isShowdata) {
			Contestant contestant = participentsList.stream().filter(i -> i.getUser().getId().equals(user.getId()))
					.findFirst().get();
			model.addAttribute("contestant", contestant);
		}
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& (user.getUserRoles().getRole().getName().equals("judge"))) {
			model.addAttribute("isJudge", true);
		}
		model.addAttribute("joincontest", isjoinConstent);
		model.addAttribute("contest", contest);
		return "contestpage";

	}

	@GetMapping("/joinContest/{id}")
	public String joinContest(Model model, @PathVariable Long id, @AuthenticationPrincipal User user) {
		if (id != null) {
			customDAO.joinContestant(id, user.getId());
		}
		return "redirect:/contest/" + id;

	}

	@PostMapping(value = "/refreshData")
	public String referData(HttpServletRequest request, Model model) throws Exception {
		myDataBaseConfiguration.createDefaultDB();
		model.addAttribute("isAdmin", true);
		model.addAttribute("refresh", true);

		return "home";
	}

	@GetMapping("/submission/{id}")
	public String SubmissionContestantData(@ModelAttribute("contestant") Contestant contestant, @PathVariable Long id,
			@ModelAttribute("contest") CreateConstestModel contest) {
		if (id != null) {
			customDAO.joinSubmission(id, contestant.getDataArea());
		}
		return "redirect:/contestList";

	}

	@PostMapping(value = "/getGradeSubmissionForm")
	public String getGradeSubmissionForm(@ModelAttribute("contestant") Contestant contestant,
			HttpServletRequest request, Model model) {
		model.addAttribute("contestant", contestant);
		return "gradeSubmissionForm";
	}

	@PostMapping(value = "/gradeSubmission")
	public String gradeSubmission(@ModelAttribute("contestant") Contestant contestant, HttpServletRequest request,
			Model model) {
		customDAO.updateContestantGrade(contestant.getId(), contestant.getGrade());
		return "contestList"; // TODO: Replace with contestantList to grade another contestant
	}

//	@GetMapping(value = "/distributeRewards")
//	public String distributeRewards(@ModelAttribute("contest") Contest contest, HttpServletRequest request,
//			Model model) {
//		final Long sponserAmount = contest.getSponserAmount();
//		List<Judge> judges = contest.getJudgeList();
//		List<Contestant> contestants = contest.getContestant();
//		final Long totalRewardForJudges = (JUDGE_TOTAL_SHARE_PERCENTAGE * sponserAmount) / 100;
//		final Long rewardPerJudge = totalRewardForJudges / judges.size();
//		final Long totalRewardForContestants = sponserAmount - totalRewardForJudges;
//		final Long rewardForWinner = (70 * totalRewardForContestants) / 100;
//		final Long rewardForRunner = totalRewardForContestants - rewardForWinner;
//
//		judges.stream().forEach(judge -> {
//			customDAO.updateWalletData(judge.getUser().getId(), rewardPerJudge);
//			Wallet wallet = customDAO.findWalletByUserId(judge.getUser().getId());
//			customDAO.saveLedgerData(wallet.getId(), rewardPerJudge, "Contest Fee");
//		});
//		Collections.sort(contestants, Comparator.comparing(Contestant::getGrade));
//		Collections.sort(contestants, Collections.reverseOrder());
//		Contestant winner = contestants.get(0);
//		Contestant runner = contestants.get(1);
//
//		Wallet wallet1 = customDAO.findWalletByUserId(winner.getUser().getId());
//		customDAO.updateWalletData(winner.getUser().getId(), wallet1.getBalance() + rewardForWinner);
//		customDAO.saveLedgerData(wallet1.getId(), rewardForWinner, "Contest Winner");
//
//		Wallet wallet2 = customDAO.findWalletByUserId(runner.getUser().getId());
//		customDAO.updateWalletData(runner.getUser().getId(), wallet2.getBalance() + rewardForRunner);
//		customDAO.saveLedgerData(wallet2.getId(), rewardForRunner, "Contest Runner");
//
//		Sponser sponser = contest.getSponser();
//		Wallet wallet3 = customDAO.findWalletByUserId(sponser.getUser().getId());
//		final Long balanceAfterDistribution = wallet3.getBalance() - sponserAmount;
//		customDAO.updateWalletData(sponser.getUser().getId(), balanceAfterDistribution);
//		customDAO.saveLedgerData(wallet1.getId(), -sponserAmount, "Contest Sponsership amount");
//
//		return "contestList";
//	}

}
