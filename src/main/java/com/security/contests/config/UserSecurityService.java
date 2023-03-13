package com.security.contests.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;
import com.security.contests.repository.CustomDAO;

@Service
public class UserSecurityService implements UserDetailsService {
	
	@Autowired
	private CustomDAO customDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = customDAO.findByUseName(username);
		if (null == user) {
			userNotFound();
		} else if (user.getId() != null) {
			UserRole userRole = customDAO.findByUserIdUserRole(user);
			if (null != userRole) {
				user.setUserRoles(userRole);
				if (userRole.getUserRoleId() != null) {
					userRole.setRole(customDAO.findByRoleId(userRole.getRole().getRoleId()));
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
