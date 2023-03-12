package com.security.contests.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.security.contests.domain.Contest;
import com.security.contests.domain.CreateConstestModel;
import com.security.contests.domain.JudgeDisplay;
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
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
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
		Object[] ob = (Object[]) query.getSingleResult();
		Role role = new Role();
		role.setRoleId((Long) ob[0]);
		role.setName((String) ob[1]);
		return role;

	}

	@Override
	public ArrayList<JudgeDisplay> listJudges() {
		final String checkSql = "select count(*) from user u , user_role ur , role r where u.id = ur.user_id and "
				+ "ur.role_id = r.role_id and r.name = 'judge'";
		Query checkquery = em.createNativeQuery(checkSql);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select u.id, u.username from user u , user_role ur , role r where u.id = ur.user_id and "
					+ "ur.role_id = r.role_id and r.name = 'judge'";
			Query query = em.createNativeQuery(sql);
			List<Object> objList = query.getResultList();
			ArrayList<JudgeDisplay> list = new ArrayList<JudgeDisplay>();
			for (Object iter : objList) {
				Object[] ob = (Object[]) iter;
				JudgeDisplay jd = new JudgeDisplay();
				jd.setJudgeName((String) ob[1]);
				jd.setJudgeUserId((Long) ob[0]);
				list.add(jd);
			}
			return list;
		}
		return null;
	}

	public int saveContestData(CreateConstestModel ccm) {
		final String sql = "INSERT INTO contest(name,start_date,end_date) values(?,?,?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, ccm.getFirstname());
		query.setParameter(2, ccm.getStartDate());
		query.setParameter(3, ccm.getEndDate());
		return query.executeUpdate();
	}
	
	@Override
	public Contest findByContest(String name) {
		final String checkSql = "select count(*) from contest where name = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, name);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select * from Contest where name = (?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, name);
			Object[] ob = query.getSingleResult() != null ? (Object[]) query.getSingleResult() : null;
			Contest contest = new Contest();
			if (ob != null) {
				contest.setId((Long) ob[0]);
				contest.setName((String) ob[2]);
				return contest;
			} else {
				return null;
			}
		}
		return null;
	} 
	
	public int saveContestJudge(Contest contestName, CreateConstestModel ccm) {
		int count = 0;
		List<JudgeDisplay> selectedJudgesList = ccm.getJdlist().stream().filter(i -> i.isJudgeSno()).toList();
		for (JudgeDisplay iter : selectedJudgesList) {
			final String sql = "INSERT INTO judge(contest_id,user_id) values(?,?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, contestName.getId());
			query.setParameter(2, iter.getJudgeUserId());
			count = count + query.executeUpdate();
		}
		return count;
	}
	
	public ArrayList<Contest> listContests(){
		final String checkSql = "select count(*) from contest";
		Query checkquery = em.createNativeQuery(checkSql);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select * from contest";
			Query query = em.createNativeQuery(sql);
			List<Object> objList = query.getResultList();
			ArrayList<Contest> list = new ArrayList<Contest>();
			for (Object iter : objList) {
				Object[] ob = (Object[]) iter;
				Contest jd = new Contest();
				jd.setId((Long)ob[0]);
				jd.setName((String) ob[2]);
				jd.setEndDate((Date)ob[1]);
				jd.setStartDate((Date)ob[3]);
				list.add(jd);
			}
			return list;
		}
		return null;
	}
}
