package com.security.contests.repository;

import org.springframework.data.repository.CrudRepository;

import com.security.contests.domain.Wallet;

public interface WalletRepositry extends CrudRepository<Wallet, Long> {

}
