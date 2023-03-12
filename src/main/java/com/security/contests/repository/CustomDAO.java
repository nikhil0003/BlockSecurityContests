package com.security.contests.repository;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.security.contests.domain.Contest;
import com.security.contests.domain.CreateConstestModel;
import com.security.contests.domain.JudgeDisplay;
import com.security.contests.domain.Role;

@Component
public interface CustomDAO {

	int saveUserData(String username, String password);

	int saveRoleData(String name);

	int saveUserRoleData(Long userId, Long roleId);

	int updateContestantGrade(Long contestantId, Long grade);

	Role findByname(String name);

	Role findByRoleId(Long roleId);

	ArrayList<JudgeDisplay> listJudges();

	int saveContestData(CreateConstestModel ccm);

	Contest findByContest(String name);

	int saveContestJudge(Contest contestName, CreateConstestModel ccm);

	ArrayList<Contest> listContests();

	Contest findByContestId(Long Id);

}
