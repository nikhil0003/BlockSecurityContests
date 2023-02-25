package com.security.contests.repository;

import org.springframework.data.repository.CrudRepository;

import com.security.contests.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);

	User findByEmail(String userEmail);
}
