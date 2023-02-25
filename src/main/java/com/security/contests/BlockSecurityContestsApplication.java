package com.security.contests;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.security.contests.config.SecurityUtility;
import com.security.contests.domain.Role;
import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;
import com.security.contests.service.UserService;

@SpringBootApplication
public class BlockSecurityContestsApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(BlockSecurityContestsApplication.class, args);
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setUsername("admin");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
	
		Set<UserRole> userRoles = new HashSet<>();
		Role role1= new Role();
		role1.setName("admin");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
	}

}
