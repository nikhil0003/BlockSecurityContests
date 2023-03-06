package com.security.contests.repository;

import org.springframework.stereotype.Component;

@Component
public interface CustomDAO {
	
	int saveUserData(String username, String password);
	
	int saveRoleData(String name);

	int saveUserRoleData(Long userId,Long roleId);
	
}
