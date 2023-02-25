package com.security.contests.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Ledger {

	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id", nullable = false, updatable = false)
	private Long Id;
	
	private String description;
	
	private Long amount;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="address_id")
	private Wallet wallet;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the amount
	 */
	public Long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

	/**
	 * @return the wallet
	 */
	public Wallet getWallet() {
		return wallet;
	}

	/**
	 * @param wallet the wallet to set
	 */
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	
	
	
}
