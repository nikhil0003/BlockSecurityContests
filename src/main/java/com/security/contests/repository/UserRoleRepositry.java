package com.security.contests.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.security.contests.domain.UserRole;

public interface UserRoleRepositry extends CrudRepository<UserRole,Long> {
	
	@Query(value ="select * from user_role where user_id = :user_id",nativeQuery = true)
	UserRole findByUserId(@Param("user_id") Long userid);
	
	@Query(value="INSERT INTO role(user_id,role_id) values(:userId , :roleId)",nativeQuery = true)
	UserRole saveRole(Long userId,Long  roleId);
	
}
