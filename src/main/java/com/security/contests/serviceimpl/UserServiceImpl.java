package com.security.contests.serviceimpl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.contests.domain.Role;
import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;
import com.security.contests.repository.CustomDAO;
import com.security.contests.repository.RoleRepository;
import com.security.contests.repository.UserRepository;
import com.security.contests.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CustomDAO customDao;

	@Override
	public User createUser(User user, UserRole userRoles) {
		User localUser = userRepository.findByUsername(user.getUsername());
		int userSuccess = 0;
		int roleSuccess = 0;
		int userRoleSuccess = 0;

		if (localUser != null) {
			LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
		} else {
			userSuccess = customDao.saveUserData(user.getUsername(), user.getPassword());
			if (userSuccess != 0) {
				roleSuccess = customDao.saveRoleData(user.getUserRoles().getRole().getName());
				if (roleSuccess != 0) {
					localUser = userRepository.findByUsername(user.getUsername());
					Role roledb = roleRepository.findByname(user.getUserRoles().getRole().getName());
					userRoleSuccess = customDao.saveUserRoleData(localUser.getId(), roledb.getRoleId());
					localUser.setUserRoles(userRoles);
				}
			}

		}

		return localUser;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
