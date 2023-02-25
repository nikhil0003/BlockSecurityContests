package com.security.contests.service;

import java.util.Set;

import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;

public interface UserService {
	User createUser(User user, Set<UserRole> userRoles) throws Exception;

	User save(User user);

	User findByUsername(String username);
}
