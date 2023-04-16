package com.security.contests.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.security.contests.domain.JudgeReview;
import com.security.contests.domain.Sponser;
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
				&& user.getUserRoles().getRole().getName() != null) {
			if(user.getUserRoles().getRole().getName().equals("sponser")) {
				model.addAttribute("isSponser", true);
			}
			model.addAttribute("roleName", user.getUserRoles().getRole().getName());
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
		List<User> topJudges = customDAO.getTopJudges();
		List<User> bigContestants = customDAO.getBigContestants();
		List<Contest> commonContests = customDAO.getCommonContests(8L, 9L);
		List<User> sleepyContestants = customDAO.getSleepyContestants();
		List<User> busyJudges = customDAO.getBusyJudges();
		List<Contest> toughContests = customDAO.getToughContests();
		List<User> contestants = customDAO.getContestants();

		final Long totalNumberOfSponsersForPastContests = customDAO.getTotalNumberOfSponsersForPastContests();
		final Long totalNumberOfJudgesForPastContests = customDAO.getTotalNumberOfJudgesForPastContests();
		final Long totalNumberOfContestantsForPastContests = customDAO.getTotalNumberOfContestantsForPastContests();
		final Long totalNumberOfPastContests = customDAO.getTotalNumberOfPastContests();
		final BigDecimal totalSponserAmount = customDAO.getTotalSponserAmount();
		final BigDecimal totalRewardForJudges = customDAO.getTotalRewardForJudges();
		final BigDecimal totalRewardForContestants = customDAO.getTotalRewardForContestants();

		model.addAttribute("bigSponsors", bigSponsors);
		model.addAttribute("topJudges", topJudges);
		model.addAttribute("bigContestants", bigContestants);
		model.addAttribute("commonContests", commonContests);
		model.addAttribute("sleepyContestants", sleepyContestants);
		model.addAttribute("busyJudges", busyJudges);
		model.addAttribute("toughContests", toughContests);
		model.addAttribute("contestants", contestants);
		model.addAttribute("totalNumberOfSponsersForPastContests", totalNumberOfSponsersForPastContests);
		model.addAttribute("totalNumberOfJudgesForPastContests", totalNumberOfJudgesForPastContests);
		model.addAttribute("totalNumberOfContestantsForPastContests", totalNumberOfContestantsForPastContests);
		model.addAttribute("totalNumberOfPastContests", totalNumberOfPastContests);
		model.addAttribute("totalSponserAmount", totalSponserAmount);
		model.addAttribute("totalRewardForJudges", totalRewardForJudges);
		model.addAttribute("totalRewardForContestants", totalRewardForContestants);

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

		List<Judge> judges = customDAO.findJudgesBycontestId(id, contestData);
		List<JudgeGradeDispaly> displayJudges = new ArrayList<>();
		judges.forEach(judge -> {
			JudgeGradeDispaly judgeGradeDispaly = new JudgeGradeDispaly();
			judgeGradeDispaly.setJudgeId(judge.getUser().getId());
			judgeGradeDispaly.setJudgeName(judge.getUser().getUsername());
			displayJudges.add(judgeGradeDispaly);
		});
		model.addAttribute("displayJudges", displayJudges);


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

	@GetMapping(value = "/close/{id}")
	public String closeContest(HttpServletRequest request, Model model, @PathVariable Long id) {
		Contest contest = customDAO.findByContestId(id);
		customDAO.closeContest(id);
		return "redirect:/contestList";
	}

	@GetMapping(value = "/distributeRewards/{id}")
	public String distributeRewards(HttpServletRequest request, Model model, @PathVariable Long id) {
		Contest contest = customDAO.findByContestId(id);
		if (contest.getSponserAmount() != null
				&& (contest.getEndDate().compareTo(new Date()) < 0 || contest.getClosed() != null)) {
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

	@GetMapping("/profile")
	public String getProfile(HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {
		String roleName = "";
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null) {
			roleName = user.getUserRoles().getRole().getName();
			model.addAttribute("userRoleName", roleName);
		}

		Wallet wallet = customDAO.findWalletByUserId(user.getId());
		model.addAttribute("walletBalance", wallet.getBalance());
		model.addAttribute("username", user.getUsername());
		switch (roleName) {
			case "sponser" :
				ArrayList<Contest> contestList = customDAO.listContestsBySponser(user.getId());
				model.addAttribute("contestList", contestList);
				break;
			case "judge" :
				ArrayList<JudgeReview> judgeReviewList = customDAO.listJudgeReviewsByJudge(user.getId());
				model.addAttribute("judgeReviewList", judgeReviewList);
				break;
			case "contestant" :
				ArrayList<Contest> contestantContestList = customDAO.listContestsByContestant(user.getId());
				model.addAttribute("contestantContestList", contestantContestList);
				break;
		}

		return "profilePage";
	}

	@GetMapping("/profile/{judgeUserId}")
	public String judgeProfile(@PathVariable("judgeUserId") Long judgeUserId, HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {
		String roleName = "";
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null) {
			roleName = user.getUserRoles().getRole().getName();
			model.addAttribute("userRoleName", roleName);
		}
		final User judgeUser = customDAO.getUser(judgeUserId);
		Wallet wallet = customDAO.findWalletByUserId(judgeUser.getId());
		model.addAttribute("walletBalance", wallet.getBalance());
		model.addAttribute("username", judgeUser.getUsername());

		ArrayList<JudgeReview> judgeReviewList = customDAO.listJudgeReviewsByJudge(judgeUser.getId());
		model.addAttribute("judgeReviewList", judgeReviewList);

		return "judgeProfilePageForSponser";
	}


	@GetMapping("/reviewSubmission/{judgeUserId}")
	public String reviewJudge(@PathVariable("judgeUserId") Long judgeUserId, @AuthenticationPrincipal User user,
									  Model model) {
		if (user.getUserRoles() != null && user.getUserRoles().getRole() != null
				&& user.getUserRoles().getRole().getName() != null
				&& (user.getUserRoles().getRole().getName().equals("sponser"))) {
			JudgeReview existingJudgeReview = customDAO.findByJudgeReviewByJudgeIdAndSponserId(judgeUserId, user.getId());
			JudgeReview judgeReview = new JudgeReview();
			judgeReview.setJudgeUserId(judgeUserId);
			judgeReview.setSponserUserId(user.getId());
			model.addAttribute("judgeReview", existingJudgeReview != null
					? existingJudgeReview
					: judgeReview);
			model.addAttribute("judgeUserId", judgeUserId);

			return "reviewSubmission";

		} else
			return "redirect:/contestList";

	}

	@PostMapping("/submitReview")
	public String submitReview(@ModelAttribute("judgeReview") JudgeReview judgeReview, @AuthenticationPrincipal User user, Model model) {
		JudgeReview existingJudgeReview = customDAO.findByJudgeReviewByJudgeIdAndSponserId(judgeReview.getJudgeUserId(), judgeReview.getSponserUserId());
		if (existingJudgeReview != null) {
			customDAO.updateJudgeReview(judgeReview.getJudgeUserId(), judgeReview.getSponserUserId(), judgeReview.getReviewScore());
		} else {
			customDAO.saveJudgeReview(judgeReview);
		}
		return "redirect:/contestList";
	}

	@GetMapping("/leaderboard")
	public String getLeaderboard(HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {
		List<User> contestantList = customDAO.getContestantsSortedDescByRewardBalance();
		model.addAttribute("contestantList", contestantList);

		return "leaderboard";
	}

	@GetMapping(value = "/deleteJudgeReview/{judgeUserId}/{sponserUserId}")
	public String deleteJudgeReview(HttpServletRequest request, Model model, @PathVariable Long judgeUserId, @PathVariable Long sponserUserId) {
		JudgeReview existingJudgeReview = customDAO.findByJudgeReviewByJudgeIdAndSponserId(judgeUserId, sponserUserId);
		if (existingJudgeReview != null) {
			customDAO.deleteJudgeReview(judgeUserId, sponserUserId);
		}
		return "redirect:/contestList";
	}
}
