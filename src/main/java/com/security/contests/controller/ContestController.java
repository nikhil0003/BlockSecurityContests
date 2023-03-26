package com.security.contests.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.security.contests.domain.Grade;
import com.security.contests.domain.Judge;
import com.security.contests.domain.JudgeDisplay;
import com.security.contests.domain.JudgeGradeDispaly;
import com.security.contests.domain.User;
import com.security.contests.domain.Wallet;
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
		Wallet wallet = customDAO.findWalletByUserId(user.getId());
		model.addAttribute("walletBalance", wallet.getBalance());
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
	public String createContest(@ModelAttribute("ccm") CreateConstestModel ccm, Model model, @AuthenticationPrincipal User user) {
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

				int sponserSave = customDAO.saveSponserData(contestName.getId(), user.getId());
			}

		} else {
			model.addAttribute("contestAlreadyFound", true);

		}

		if (saveContestJudge > 0) {

			Wallet wallet = customDAO.findWalletByUserId(user.getId());
			customDAO.updateWalletData(user.getId(), wallet.getBalance()-ccm.getSponserAmount());
			customDAO.saveLedgerData(wallet.getId(), -ccm.getSponserAmount(), "Contest Debited");

		
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

	@GetMapping("/dashboard")
	public String getDashboard(Model model) {
		List<User> bigSponsors = customDAO.getBigSponsers();
		List<User> bigContestants = customDAO.getBigContestants();
		List<Contest> commonContests = customDAO.getCommonContests(8L, 9L);
		List<User> sleepyContestants = customDAO.getSleepyContestants();
		List<User> busyJudges = customDAO.getBusyJudges();
		List<Contest> toughContests = customDAO.getToughContests();
		List<User> contestants = customDAO.getContestants();

		model.addAttribute("bigSponsors", bigSponsors);
		model.addAttribute("bigContestants", bigContestants);
		model.addAttribute("commonContests", commonContests);
		model.addAttribute("sleepyContestants", sleepyContestants);
		model.addAttribute("busyJudges", busyJudges);
		model.addAttribute("toughContests", toughContests);
		model.addAttribute("contestants", contestants);

		return "dashboard";
	}

	@PostMapping("/copyCats")
	public String findCopyCats(Model model, @ModelAttribute("userId") Long userId) {
		List<User> copyCats = customDAO.getCopyCats(userId);
		model.addAttribute("copyCats", copyCats);
		return "copyCats";
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
			contest.setSponserAmount(contestData.getSponserAmount());
		}

		boolean haveTojoin = false;
		boolean joinedConstanst = false;
		boolean isContestUser = false;

		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& user.getUserRoles().getRole().getName().equals("contestant")
				&& contestData.getEndDate().compareTo(new Date()) > 0) {
			isContestUser = true;

		}

		ArrayList<Contestant> participentsListDb = customDAO.listContestantForContest(contestData);
		ArrayList<JudgeGradeDispaly> participentsList = new ArrayList<>();
		if (!participentsListDb.isEmpty()
				&& participentsListDb.stream().anyMatch(i -> i.getUser().getId().equals(user.getId()))) {
			// already joined
			if (isContestUser) {
				joinedConstanst = true;
			}

		} else {
			if (isContestUser)
				haveTojoin = true;
		}

		if (!participentsListDb.isEmpty() && isContestUser) {
			Optional<Contestant> contestantOp = participentsListDb.stream()
					.filter(i -> i.getUser().getId().equals(user.getId())).findFirst();
			if (contestantOp.isPresent()) {
				Contestant contestant = contestantOp.get();
				model.addAttribute("contestant", contestant);
			}

		}

		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& (user.getUserRoles().getRole().getName().equals("judge"))) {
			model.addAttribute("isJudge", true);

		}
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& user.getUserRoles().getRole().getName().equals("sponser")) {
			model.addAttribute("isSponser", true);
		}
		if (!participentsListDb.isEmpty()) {
			participentsListDb.forEach(i -> {
				JudgeGradeDispaly jgd = new JudgeGradeDispaly();
				jgd.setDataArea(i.getDataArea());
				jgd.setContestantId(i.getId());
				jgd.setJudgeId(user.getId());
				jgd.setContestId(id);
				participentsList.add(jgd);
			});
			model.addAttribute("participentsList", participentsList);

		}
		model.addAttribute("haveTojoin", haveTojoin);
		model.addAttribute("joinedConstanst", joinedConstanst);
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

	@GetMapping("/gradeSubmission/{id}/{contestId}")
	public String gradeContestantData(@PathVariable("id") Long contestantId, @AuthenticationPrincipal User user,
			Model model, @PathVariable("contestId") Long contestId) {
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& (user.getUserRoles().getRole().getName().equals("judge")) && contestantId != null) {
			Contestant contestant = customDAO.findByContestantId(contestantId);
			JudgeGradeDispaly jgd = new JudgeGradeDispaly();
			jgd.setContestantId(contestant.getId());
			jgd.setDataArea(contestant.getDataArea());
			jgd.setJudgeId(user.getId());
			model.addAttribute("jdg", jgd);
			model.addAttribute("contestId", contestId);
			Object[] grade = customDAO.getGradeData(jgd, contestant, user.getId(), contestId);
			if (grade != null) {
				model.addAttribute("alreadyGraded", true);
			} else {
				model.addAttribute("alreadyGraded", false);
			}
			return "gradeSubmission";

		} else
			return "redirect:/contestList";

	}

	@PostMapping("/submitGrade/{contestId}")
	public String SubmitGrade(@ModelAttribute("jdg") JudgeGradeDispaly jgd, @AuthenticationPrincipal User user,
			@PathVariable("contestId") Long contestId, Model model) {
		Contestant contestant = customDAO.findByContestantId(jgd.getContestantId());
		Object[] grade = customDAO.getGradeData(jgd, contestant, user.getId(), contestId);
		if (grade != null) {
			model.addAttribute("alreadyGraded", true);
		} else {
			int success = customDAO.saveGradewithJudgeId(jgd, contestant, user.getId(), contestId);
			model.addAttribute("alreadyGraded", false);
		}
		return "redirect:/contestList";
	}

	@GetMapping(value = "/distributeRewards/{id}")
	public String distributeRewards(HttpServletRequest request, Model model, @PathVariable Long id) {
		Contest contest = customDAO.findByContestId(id);
		if (contest.getSponserAmount() != null && contest.getEndDate().compareTo(new Date())<0) {
			ArrayList<Contestant> participentsListDb = customDAO.listContestantForContest(contest);
			List<Judge> judges = customDAO.findJudgesBycontestId(id, contest);
			if (judges != null && !judges.isEmpty()) {
				Long judgesAmount = (long) (contest.getSponserAmount() * 0.20);
				Long eachJudgeAmount = judgesAmount / judges.size();
				judges.forEach(i -> {
					Wallet wallet = customDAO.findWalletByUserId(i.getUser().getId());
					customDAO.updateWalletData(i.getUser().getId(), wallet.getBalance()+eachJudgeAmount);
					customDAO.saveLedgerData(wallet.getId(), eachJudgeAmount, "Contest Rewards");

				});
			}
			Long contestsAmount = (long) (contest.getSponserAmount() * 0.80);
			List<Grade> grades = customDAO.findGradesBycontestId(id, contest);
			Map<Long, List<Grade>> participantGrades = grades.stream().collect(Collectors.groupingBy(Grade::getUserId));
			Map<Long, Long> participantAvergaeGrades = new HashMap<>();
			Map<Long, Long> participantAmount = new HashMap<>();
			participantGrades.forEach((k, v) -> {
				double avergaeGrade = v.stream().filter(i -> i != null && i.getGradeValue() != null)
						.mapToLong(mapper -> mapper.getGradeValue()).average().orElse(0L);
				participantAvergaeGrades.put(k, (long) avergaeGrade);
			});
			Long totalGrade = participantAvergaeGrades.values().stream().mapToLong(Long::longValue)
					  .sum();
			Long eachPart = contestsAmount / totalGrade;
			// save Contest Amount
			participantAvergaeGrades.forEach((k, v) -> {
				participantAmount.put(k, v * eachPart);
				Wallet wallet = customDAO.findWalletByUserId(k);
				customDAO.updateWalletData(k, wallet.getBalance()+(v * eachPart));
				customDAO.saveLedgerData(wallet.getId(), v * eachPart, "Contest Rewards");

			});
		}
		return "redirect:/contestList";
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
