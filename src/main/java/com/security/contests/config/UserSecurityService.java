package com.security.contests.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;
import com.security.contests.repository.RoleRepository;
import com.security.contests.repository.UserRepository;
import com.security.contests.repository.UserRoleRepositry;

@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepositry userRoleRepositry;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (null == user) {
			userNotFound();
		} else if (user.getId() != null) {
			UserRole userRole = userRoleRepositry.findByUserId(user.getId());
			if (null != userRole) {
				if (userRole.getUserRoleId() != null) {
					userRole.setRole(roleRepository.findByRoleId(userRole.getUserRoleId()));
				} else {
					userNotFound();
				}
			} else {
				userNotFound();
			}
		}
		return user;
	}

	private void userNotFound() {
		throw new UsernameNotFoundException("Username not found");
	}

}
