package com.security.contests.repository;

import org.springframework.data.repository.CrudRepository;

import com.security.contests.domain.Role;


public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByname(String name);
}
