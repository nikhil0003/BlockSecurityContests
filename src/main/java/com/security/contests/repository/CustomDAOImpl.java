package com.security.contests.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
@Transactional
@Component
public class CustomDAOImpl implements CustomDAO {

	@Autowired
	private EntityManager em;

	@Override
	public int saveUserData(String username, String password) {
		final String sql = "INSERT INTO user(username,password,enabled) values(?,?,true)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, username);
		query.setParameter(2, password);
		return query.executeUpdate();
	}

	@Override
	public int saveRoleData(String name) {
		final String sql = "INSERT INTO role(name) values(?)";
		Query query = em.createNativeQuery(sql);
        query.setParameter(1,name);
      return query.executeUpdate();		
	}
	
	@Override
	public int saveUserRoleData(Long userId,Long roleId) {
		final String sql = "INSERT INTO user_role(role_id,user_id) values(?,?)";
		Query query = em.createNativeQuery(sql);
        query.setParameter(2,userId);
        query.setParameter(1,roleId);
      return query.executeUpdate();		
	}

	

}
