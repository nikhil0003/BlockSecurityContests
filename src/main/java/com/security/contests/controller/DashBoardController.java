package com.security.contests.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.contests.domain.Contest;
import com.security.contests.domain.User;
import com.security.contests.repository.CustomDAO;

@RestController
public class DashBoardController {

	@Autowired
	private CustomDAO customDAO;
	
	
	@GetMapping("/bigSponsors")
	public String getbigSponsors(Model model) {
		List<User> bigSponsors = customDAO.getBigSponsers();
				model.addAttribute("bigSponsors", bigSponsors);
		return "bigSponsors";
	}
	
	@GetMapping("/topJudges")
	public String getTopJudges(Model model) {
		List<User> topJudges = customDAO.getTopJudges();
		model.addAttribute("topJudges", topJudges);
		return "bigSponsors";
	}
	@GetMapping("/bigContestants")
	public String getBigContestants(Model model) {
		List<User> bigContestants = customDAO.getBigContestants();
		model.addAttribute("bigContestants", bigContestants);
		return "bigContestants";
	}
	@GetMapping("/commonContests")
	public String getCommonContests(Model model) {
		List<Contest> commonContests = customDAO.getCommonContests(8L, 9L);
		model.addAttribute("commonContests", commonContests);
		return "commonContests";
	}
	
	
	@GetMapping("/sleepyContestants")
	public String getsleepyContestants(Model model) {
		List<User> sleepyContestants = customDAO.getSleepyContestants();
		model.addAttribute("sleepyContestants", sleepyContestants);
		return "sleepyContestants";
	}
	@GetMapping("/busyJudges")
	public String getbusyJudges(Model model) {
		List<User> busyJudges = customDAO.getBusyJudges();
		model.addAttribute("busyJudges", busyJudges);
		return "bigSponsors";
	}
	
	
	@GetMapping("/toughContests")
	public String gettoughContests(Model model) {
		List<Contest> toughContests = customDAO.getToughContests();
		model.addAttribute("toughContests", toughContests);
		return "toughContests";
	}
	
	
	@GetMapping("/copycontestants")
	public String contestants(Model model) {
		List<User> contestants = customDAO.getContestants();
		model.addAttribute("contestants", contestants);
		return "copycontestants";
	}
	
	@GetMapping("/statistics")
	public String statistics(Model model) {
		final Long totalNumberOfSponsersForPastContests = customDAO.getTotalNumberOfSponsersForPastContests();
		final Long totalNumberOfJudgesForPastContests = customDAO.getTotalNumberOfJudgesForPastContests();
		final Long totalNumberOfContestantsForPastContests = customDAO.getTotalNumberOfContestantsForPastContests();
		final Long totalNumberOfPastContests = customDAO.getTotalNumberOfPastContests();
		final BigDecimal totalSponserAmount = customDAO.getTotalSponserAmount();
		final BigDecimal totalRewardForJudges = customDAO.getTotalRewardForJudges();
		final BigDecimal totalRewardForContestants = customDAO.getTotalRewardForContestants();
		
		model.addAttribute("totalNumberOfSponsersForPastContests", totalNumberOfSponsersForPastContests);
		model.addAttribute("totalNumberOfJudgesForPastContests", totalNumberOfJudgesForPastContests);
		model.addAttribute("totalNumberOfContestantsForPastContests", totalNumberOfContestantsForPastContests);
		model.addAttribute("totalNumberOfPastContests", totalNumberOfPastContests);
		model.addAttribute("totalSponserAmount", totalSponserAmount);
		model.addAttribute("totalRewardForJudges", totalRewardForJudges);
		model.addAttribute("totalRewardForContestants", totalRewardForContestants);
		return "statistics";
	}

}
