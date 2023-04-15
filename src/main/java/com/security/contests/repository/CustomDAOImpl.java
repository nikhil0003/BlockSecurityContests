package com.security.contests.repository;

import com.security.contests.domain.Contest;
import com.security.contests.domain.Contestant;
import com.security.contests.domain.CreateConstestModel;
import com.security.contests.domain.Grade;
import com.security.contests.domain.Judge;
import com.security.contests.domain.JudgeDisplay;
import com.security.contests.domain.JudgeGradeDispaly;
import com.security.contests.domain.Role;
import com.security.contests.domain.User;
import com.security.contests.domain.UserRole;
import com.security.contests.domain.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	public int saveWalletData(Long userId, Long balance) {
		final String sql = "INSERT INTO wallet(balance,user_id) values(?,?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(2, userId);
		query.setParameter(1, balance);
		return query.executeUpdate();
	}

	@Override
	public int updateWalletData(Long userId, Long balance) {
		final String sql = "UPDATE wallet SET balance = ? WHERE user_id = ?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(2, userId);
		query.setParameter(1, balance);
		return query.executeUpdate();
	}

	@Override
	public int saveLedgerData(Long walletId, Long amount, String description) {
		final String sql = "INSERT INTO ledger(address_id, amount, description) values(?,?, ?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, walletId);
		query.setParameter(2, amount);
		query.setParameter(3, description);
		return query.executeUpdate();
	}

	@Override
	public int updateContestantGrade(Long contestantId, Long grade) {
		final String sql = "UPDATE contestant SET grade = ? WHERE id = ?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, grade);
		query.setParameter(2, contestantId);
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
	public Wallet findWalletByUserId(Long userId) {
		final String sql = "select * from wallet where user_id =(?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, userId);
		Object[] ob = (Object[]) query.getSingleResult();
		Wallet wallet = new Wallet();
		wallet.setId((Long) ob[0]);
		// wallet.setAddress((String)ob[1]);
		wallet.setBalance((Long) ob[2]);
		return wallet;
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
		final String sql = "INSERT INTO contest(name,start_date,end_date,sponserAmount) values(?,?,?,?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, ccm.getFirstname());
		query.setParameter(2, ccm.getStartDate());
		query.setParameter(3, ccm.getEndDate());
		query.setParameter(4, ccm.getSponserAmount());
		return query.executeUpdate();
	}

	@Override
	public int saveSponserData(Long contestId, Long userId) {
		final String sql = "INSERT INTO sponser(contest_id, user_id) values(?,?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, contestId);
		query.setParameter(2, userId);
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

	public ArrayList<Contest> listContests() {
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
				jd.setId((Long) ob[0]);
				jd.setName((String) ob[2]);
				jd.setEndDate((Date) ob[1]);
				jd.setStartDate((Date) ob[3]);
				list.add(jd);
			}
			return list;
		}
		return null;
	}

	@Override
	public ArrayList<Contest> listContestsBySponser(final Long sponserUserId) {
		final String checkSql = "select count(*) from sponser s inner join contest c on c.id = s.contest_id where s.user_id = ?";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, sponserUserId);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select c.* from sponser s inner join contest c on c.id = s.contest_id where s.user_id = ? order by c.start_date desc";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, sponserUserId);
			List<Object> objList = query.getResultList();
			ArrayList<Contest> list = new ArrayList<Contest>();
			for (Object iter : objList) {
				Object[] ob = (Object[]) iter;
				Contest jd = new Contest();
				jd.setId((Long) ob[0]);
				jd.setName((String) ob[2]);
				jd.setEndDate((Date) ob[1]);
				jd.setStartDate((Date) ob[3]);
				list.add(jd);
			}
			return list;
		}
		return null;
	}

	@Override
	public Contest findByContestId(Long Id) {
		final String checkSql = "select count(*) from contest where id = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, Id);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select * from Contest where id = (?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, Id);
			Object[] ob = query.getSingleResult() != null ? (Object[]) query.getSingleResult() : null;
			Contest jd = new Contest();
			if (ob != null) {
				jd.setId((Long) ob[0]);
				jd.setName((String) ob[2]);
				jd.setEndDate((Date) ob[1]);
				jd.setStartDate((Date) ob[3]);
				jd.setSponserAmount((Long) ob[4]);
				jd.setClosed((String) ob[5]);
				return jd;
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	public List<User> getContestants() {
		final String query = "select ur.user_id as contestant_user_id from user_role ur where ur.role_id = 3";
		Query q = em.createNativeQuery(query, Long.class);
		List<Long> list = q.getResultList();
		return getUsers(list);
	}

	@Override
	public List<User> getBigSponsers() {
		final String bigSponsersQuery = "select sponser_contests.user_id from (\n" +
				"SELECT user_id as user_id, count(distinct contest_id) as total_contests FROM sponser\n" +
				"group by user_id having count(distinct contest_id)\n" +
				") as sponser_contests where sponser_contests.total_contests = (\n" +
				"select MAX(sc.total_contests) from (\n" +
				"SELECT user_id as user_id, count(distinct contest_id) as total_contests FROM sponser\n" +
				"group by user_id having count(distinct contest_id)\n" +
				") as sc\n" +
				");";
		Query q = em.createNativeQuery(bigSponsersQuery, Long.class);
		List<Long> list = q.getResultList();

		return getUsers(list);
	}

	@Override
	public List<User> getBigContestants() {
		final String bigContestantsQuery = "select w.user_id  from wallet w where w.balance = (\n" +
				"select max(w1.balance) from wallet w1, user_role ur where ur.user_id = w1.user_id and ur.role_id = 3\n" +
				") and w.user_id in (select ur1.user_id as contestant_user_id from user_role ur1 where ur1.role_id = 3);";
		Query q = em.createNativeQuery(bigContestantsQuery, Long.class);
		List<Long> list = q.getResultList();
		return getUsers(list);
	}

	@Override
	public List<Contest> getCommonContests(final Long userId1, final Long userId2) {
		final String commonContestsQuery = "SELECT ct1.contest_id FROM contestant as ct1 where ct1.user_id in (?, ?)\n" +
				"group by ct1.contest_id having count(ct1.user_id) = 2;";
		Query q = em.createNativeQuery(commonContestsQuery, Long.class);
		q.setParameter(1, userId1);
		q.setParameter(2, userId2);
		List<Long> list = q.getResultList();
		final String sql = "select * from contest where id in (?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, list);
		List<Object> objList = query.getResultList();
		List<Contest> contests = new ArrayList<>();
		for (Object iter : objList) {
			Object[] ob = (Object[]) iter;
			Contest contest = new Contest();
			contest.setId((Long) ob[0]);
			contest.setName((String) ob[2]);
			contests.add(contest);
		}
		return contests;
	}

	@Override
	public List<User> getSleepyContestants() {
		final String sleepyContestantsQuery = "select u.id from user u inner join user_role ur on ur.user_id = u.id\n" +
				"inner join role r on r.role_id = ur.role_id\n" +
				"where r.name = 'contestant'\n" +
				"and u.id not in (SELECT user_id FROM contestant);";
		Query q = em.createNativeQuery(sleepyContestantsQuery, Long.class);
		List<Long> list = q.getResultList();
		return getUsers(list);
	}

	@Override
	public List<User> getBusyJudges() {
		final String busyJudgesQuery = "select user_id from judge group by user_id order by count(*) desc;";
		Query q = em.createNativeQuery(busyJudgesQuery, Long.class);
		List<Long> list = q.getResultList();
		return getUsers(list);
	}

	@Override
	public List<Contest> getToughContests() {
		final String toughContestsQuery = "select contest_id from contestant group by contest_id having count(user_id) < 10;;";
		Query q = em.createNativeQuery(toughContestsQuery, Long.class);
		List<Long> list = q.getResultList();
		final String sql = "select * from contest where id in (?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, list);
		List<Object> objList = query.getResultList();
		List<Contest> contests = new ArrayList<>();
		for (Object iter : objList) {
			Object[] ob = (Object[]) iter;
			Contest contest = new Contest();
			contest.setId((Long) ob[0]);
			contest.setName((String) ob[2]);
			contests.add(contest);
		}
		return contests;
	}


	@Override
	public List<User> getCopyCats(final Long user_id) {
		final String sql = "select user_id, contest_id from contestant where user_id in (\n" +
				"select user_id from contestant where contest_id in " +
				"(select contest_id from contestant where user_id = ?))";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, user_id);
		List<Object> objList = query.getResultList();
		if (!objList.isEmpty()) {
			Map<Long, List<Long>> map = new HashMap<>();
			for (Object iter : objList) {
				Object[] ob = (Object[]) iter;
				map.computeIfAbsent(((Long)ob[0]), k -> new ArrayList<>()).add((Long)ob[1]);
			}
			List<Long> contestsOfUser = map.get(user_id);
			List<Long> copyCats = map.entrySet().stream()
					.filter(e -> e.getKey() != user_id
							&& e.getValue().size() == contestsOfUser.size()
							&& contestsOfUser.containsAll(e.getValue()))
					.map(e -> e.getKey())
					.collect(Collectors.toList());

			return getUsers(copyCats);
		}
		return Collections.emptyList();
	}

	@Override
	public User getUser(final Long userId) {
		return getUsers(List.of(userId)).stream().findFirst().orElse(null);
	}

	@Override
	public List<User> getUsers(final List<Long> userIds) {
		final String sql = "select * from user where id in (?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, userIds);
		List<Object> objList = query.getResultList();
		List<User> users = new ArrayList<>();
		for (Object iter : objList) {
			Object[] ob = (Object[]) iter;
			User user = new User();
			user.setId((Long) ob[0]);
			user.setUsername((String) ob[5]);
			users.add(user);
		}
		return users;
	}

	public ArrayList<Contestant> listContestantForContest(Contest contest) {
		final String checkSql = "select count(*) from contestant where contest_id = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, contest.getId());
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0) {
			final String sql = "select * from contestant where contest_id = (?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, contest.getId());
			List<Object> objList = query.getResultList();
			ArrayList<Contestant> list = new ArrayList<Contestant>();
			for (Object iter : objList) {
				Object[] ob = (Object[]) iter;
				Contestant jd = new Contestant();
				jd.setId((Long) ob[0]);
				jd.setDataArea((String) ob[1]);
				jd.setGrade((Long) ob[2]);
				User user = new User();
				user.setId((Long) ob[4]);
				jd.setContest(contest);
				jd.setUser(user);
				list.add(jd);
			}
			return list;
		} else {
			return new ArrayList<>();
		}
	}

	public int joinContestant(Long contestId, Long userId) {
		final String sql = "INSERT INTO contestant(contest_id,user_id) values(?,?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, contestId);
		query.setParameter(2, userId);
		return query.executeUpdate();
	}

	public int joinSubmission(Long id, String data) {
		final String sql = "UPDATE contestant SET data_area = (?) WHERE id =(?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, data);
		query.setParameter(2, id);
		return query.executeUpdate();
	}

	public User findByUseName(String name) {
		final String checkSql = "select count(*) from user where username = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, name);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select * from user where username = (?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, name);
			Object[] ob = query.getSingleResult() != null ? (Object[]) query.getSingleResult() : null;
			User user = new User();
			if (ob != null) {
				user.setId((Long) ob[0]);
				user.setUsername(name);
				user.setPassword((String) ob[3]);
				return user;
			} else {
				return null;
			}
		}
		return null;
	}

	public UserRole findByUserIdUserRole(User use) {
		final String checkSql = "select count(*) from user_role where user_id = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, use.getId());
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select * from user_role where user_id = (?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, use.getId().intValue());
			Object[] ob = query.getSingleResult() != null ? (Object[]) query.getSingleResult() : null;
			UserRole user = new UserRole();
			if (ob != null) {
				user.setUserRoleId((Long) ob[0]);
				user.setUser(use);
				Role role = new Role();
				role.setRoleId((Long) ob[1]);
				user.setRole(role);
				return user;
			} else {
				return null;
			}
		}
		return null;
	}

	public Contestant findByContestantId(Long id) {

		final String checkSql = "select count(*) from contestant where id = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, id);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select * from contestant where id = (?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, id);
			Object[] ob = query.getSingleResult() != null ? (Object[]) query.getSingleResult() : null;
			Contestant contestant = new Contestant();
			if (ob != null) {
				contestant.setId((Long) ob[0]);
				contestant.setDataArea((String) ob[1]);
				contestant.setGrade((Long) ob[2]);
				User user = new User();
				user.setId((Long) ob[4]);
				contestant.setUser(user);
				return contestant;
			} else {
				return null;
			}
		}
		return null;

	}

	public int SaveGradeIdInContestant(Long contestantId, Long gradeId) {
		final String sql = "UPDATE contestant SET grade = ? WHERE id = ?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, gradeId);
		query.setParameter(2, contestantId);
		return query.executeUpdate();
	}

	public Object[] getGradeData(JudgeGradeDispaly jgd, Contestant contestant, Long judgeId, Long contestId) {
		final String checkSql = "select count(*) from grade where contestant_id = (?) and "
				+ "contest_id = (?) and user_id =(?) and judge_id =(?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, jgd.getContestantId());
		checkquery.setParameter(2, contestId);
		checkquery.setParameter(3, contestant.getUser().getId());
		checkquery.setParameter(4, judgeId);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String Sql = "select * from grade where contestant_id = (?) and "
					+ "contest_id = (?) and user_id =(?) and judge_id =(?)";
			Query query = em.createNativeQuery(Sql);
			query.setParameter(1, jgd.getContestantId());
			query.setParameter(2, contestId);
			query.setParameter(3, contestant.getUser().getId());
			query.setParameter(4, judgeId);
			Object[] ob = (Object[]) query.getSingleResult();
			return ob;
		}
		return null;
	}

	public int saveGradewithJudgeId(JudgeGradeDispaly jgd, Contestant contestant, Long judgeId, Long contestId) {
		final String sql = "INSERT INTO grade(contestant_id,contest_id,user_id,gradeValue,judge_id) values(?,?,?,?,?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, jgd.getContestantId());
		query.setParameter(2, contestId);
		query.setParameter(3, contestant.getUser().getId());
		query.setParameter(4, jgd.getGradeValue());
		query.setParameter(5, judgeId);
		return query.executeUpdate();
	}

	public ArrayList<Judge> findJudgesBycontestId(Long id, Contest contest) {
		final String checkSql = "select count(*) from contest c , judge j where c.id = j.contest_id and contest_id = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, id);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select * from contest c , judge j where c.id = j.contest_id and contest_id = (?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, id);
			List<Object> objList = query.getResultList();
			ArrayList<Judge> list = new ArrayList<Judge>();
			for (Object iter : objList) {
				Object[] ob = (Object[]) iter;
				Judge jd = new Judge();
				jd.setId((Long) ob[0]);
				User user = getUser((Long) ob[8]);
				//user.setId((Long) ob[7]);
				jd.setContest(contest);
				jd.setUser(user);
				list.add(jd);
			}
			return list;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Grade> findGradesBycontestId(Long id, Contest contest) {
		final String checkSql = "select count(*) from grade where contest_id = (?)";
		Query checkquery = em.createNativeQuery(checkSql);
		checkquery.setParameter(1, id);
		Long present = (Long) checkquery.getSingleResult();
		if (present > 0L) {
			final String sql = "select * from grade where contest_id = (?)";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, id);
			List<Object> objList = query.getResultList();
			ArrayList<Grade> list = new ArrayList<Grade>();
			for (Object iter : objList) {
				Object[] ob = (Object[]) iter;
				Grade grade = new Grade();
				grade.setId((Long) ob[0]);
				grade.setContestantId((Long)ob[1]);
				grade.setUserId((Long)ob[2]);
				grade.setContestId((Long)ob[3]);
				grade.setJudgeId((Long)ob[4]);
				grade.setGradeValue((Long)ob[5]);
				list.add(grade);
			}
			return list;
		}
		return null;
	}
	

	@Override
	public int closeContest(Long contestId) {
		final String sql = "UPDATE contest SET closed = 'false' WHERE id = ?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, contestId);
		return query.executeUpdate();
	}
}
