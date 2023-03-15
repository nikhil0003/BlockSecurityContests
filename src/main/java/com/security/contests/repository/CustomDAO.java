package com.security.contests.repository;

import java.util.ArrayList;

import com.security.contests.domain.*;
import org.springframework.stereotype.Component;

@Component
public interface CustomDAO {

	int saveUserData(String username, String password);

	int saveRoleData(String name);

	int saveUserRoleData(Long userId, Long roleId);

	int saveWalletData(Long userId, Long balance);

	int updateWalletData(Long userId, Long balance);

	int saveLedgerData(Long walletId, Long amount, String description);

	int updateContestantGrade(Long contestantId, Long grade);

	Role findByname(String name);

	Role findByRoleId(Long roleId);

	Wallet findWalletByUserId(Long userId);

	ArrayList<JudgeDisplay> listJudges();

	int saveContestData(CreateConstestModel ccm);

	Contest findByContest(String name);

	int saveContestJudge(Contest contestName, CreateConstestModel ccm);

	ArrayList<Contest> listContests();

	Contest findByContestId(Long Id);

	ArrayList<Contestant> listContestantForContest(Contest contest);
	
	int joinContestant(Long contestId,Long userId);
	
	UserRole findByUserIdUserRole(User use);
	
	User findByUseName(String name);
	
	int joinSubmission(Long id, String data);
	
    Contestant findByContestantId(Long id);
    
    Object[] getGradeData(JudgeGradeDispaly jgd, Contestant contestant, Long judgeId, Long contestId);
    
    int saveGradewithJudgeId(JudgeGradeDispaly jgd, Contestant contestant, Long judgeId, Long contestId);
    
    int SaveGradeIdInContestant(Long contestantId, Long gradeId);

}
