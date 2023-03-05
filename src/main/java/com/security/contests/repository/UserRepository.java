package com.security.contests.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.security.contests.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	@Query(value ="select * from user where username = :username",nativeQuery = true)
	User findByUsername(@Param("username") String username);
	
	
	@Query(value="INSERT INTO securitycontest.user(username,password,enabled)  values(\"hello1\" ,\"he;lo1\",true)",nativeQuery = true)
    void saveUserData(@Param("username") String username,@Param("password") String password);
	
	
	

}
