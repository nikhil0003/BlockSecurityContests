package com.security.contests.service;

import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;

public interface UserService {
	User createUser(User user,UserRole userRoles) throws Exception;

	User save(User user);

	User findByUsername(String username);
}
