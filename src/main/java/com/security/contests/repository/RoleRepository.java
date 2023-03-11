package com.security.contests.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.security.contests.domain.Role;


public interface RoleRepository extends CrudRepository<Role, Long> {
	
	
	@Query(value ="select * from role where role_Id = :roleId",nativeQuery = true)
	Role findByRoleId(@Param("roleId") Long roleId);
	
//	@Query(value="INSERT INTO role(name) values(:name)",nativeQuery = true)
//	void saveRole(@Param("name") String name);
}

