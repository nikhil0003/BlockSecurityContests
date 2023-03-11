package com.security.contests.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.security.contests.domain.Role;

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
		query.setParameter(1, name);
		return query.executeUpdate();
	}

	@Override
	public int saveUserRoleData(Long userId, Long roleId) {
		final String sql = "INSERT INTO user_role(role_id,user_id) values(?,?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(2, userId);
		query.setParameter(1, roleId);
		return query.executeUpdate();
	}

	@Override
	public Role findByname(String name) {
		final String checkSql = "select count(*) from role where name = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, name);
		Long present = (Long)checkquery.getSingleResult();
		 if(present >0L) {
			 final String sql = "select * from role where name = (?)";
				Query query = em.createNativeQuery(sql);
				query.setParameter(1, name);
				Object[] ob = query.getSingleResult() != null ? (Object[]) query.getSingleResult() : null;
				Role role = new Role();
				if (ob != null) {
					role.setRoleId((Long) ob[0]);
					role.setName((String) ob[1]);
					return role;
				} else {
					return null;
				} 
		 }
		return null;
	}
	
	@Override
	public Role findByRoleId(@Param("roleId") Long roleId) {
		final String sql = "select * from role where role_Id =(?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, roleId);
		 Object[] ob =  (Object[]) query.getSingleResult();
		 Role role = new Role();
		 role.setRoleId((Long)ob[0]);
		 role.setName((String)ob[1]);
		 return role;

	}

}
