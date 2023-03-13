package com.security.contests.domain;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id", nullable = false, updatable = false)
	private Long Id;

	private Long balance;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy = "wallet")
	private List<Ledger> ledger;

	/**
	 * @return the id
	 */
	public Long getId() {
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
	}

	/**
	 * @return the balance
	 */
	public Long getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Long balance) {
		this.balance = balance;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the ledger
	 */
	public List<Ledger> getLedger() {
		return ledger;
	}

	/**
	 * @param ledger the ledger to set
	 */
	public void setLedger(List<Ledger> ledger) {
		this.ledger = ledger;
	}
	
	

}
