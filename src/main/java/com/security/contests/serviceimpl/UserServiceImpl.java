package com.security.contests.serviceimpl;

import com.security.contests.domain.Wallet;
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

	private static final Long DEFAULT_INITIAL_BALANCE = 0L;
	private static final Long SPONSER_INITIAL_BALANCE = 1000000L;

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
		int walletSuccess = 0;

		if (localUser != null) {
			LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
			Role roledb = customDao.findByname(user.getUserRoles().getRole().getName());
			localUser.getUserRoles().setRole(roledb);
			
		} else {
			userSuccess = customDao.saveUserData(user.getUsername(), user.getPassword());
			localUser = userRepository.findByUsername(user.getUsername());
			if (userSuccess != 0) {
				Role roledb = customDao.findByname(user.getUserRoles().getRole().getName());
				if (roledb == null) {
					// save role
					roleSuccess = customDao.saveRoleData(user.getUserRoles().getRole().getName());
					if (roleSuccess != 0) {
						roledb = customDao.findByname(user.getUserRoles().getRole().getName());
						userRoleSuccess = customDao.saveUserRoleData(localUser.getId(), roledb.getRoleId());
						localUser.setUserRoles(userRoles);
					}
				} else {
					userRoleSuccess = customDao.saveUserRoleData(localUser.getId(), roledb.getRoleId());
					localUser.setUserRoles(userRoles);
				}
				final Long initialBalance = roledb.getName().equals("sponser")
						? SPONSER_INITIAL_BALANCE
						: DEFAULT_INITIAL_BALANCE;
				walletSuccess = customDao.saveWalletData(localUser.getId(), initialBalance);
				if (walletSuccess != 0) {
					Wallet wallet = customDao.findWalletByUserId(localUser.getId());
					customDao.saveLedgerData(wallet.getId(), initialBalance, "Initial balance");
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
