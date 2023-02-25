package com.security.contests.repository;

import org.springframework.data.repository.CrudRepository;

import com.security.contests.domain.UserRole;

public interface UserRoleRepositry extends CrudRepository<UserRole,Long> {

}
