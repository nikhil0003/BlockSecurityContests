package com.security.contests.repository;

import org.springframework.data.repository.CrudRepository;

import com.security.contests.domain.Contestant;

public interface ContestantRepositry extends CrudRepository<Contestant, Long> { 

}
