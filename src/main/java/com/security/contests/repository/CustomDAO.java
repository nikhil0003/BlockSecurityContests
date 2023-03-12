package com.security.contests.repository;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.security.contests.domain.JudgeDisplay;
import com.security.contests.domain.Role;

@Component
public interface CustomDAO {
	
	int saveUserData(String username, String password);
	
	int saveRoleData(String name);

	int saveUserRoleData(Long userId,Long roleId);
	
	Role findByname(String name);
	
	
	Role findByRoleId(Long roleId);
	
	ArrayList<JudgeDisplay> listJudges();
	
}
