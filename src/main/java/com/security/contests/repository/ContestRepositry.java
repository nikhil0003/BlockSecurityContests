package com.security.contests.repository;

import org.springframework.data.repository.CrudRepository;

import com.security.contests.domain.Contest;

public interface ContestRepositry extends CrudRepository<Contest,Long>{

}
