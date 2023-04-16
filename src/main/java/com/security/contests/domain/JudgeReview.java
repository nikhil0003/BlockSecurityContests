package com.security.contests.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class JudgeReview {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id", nullable = false, updatable = false)
	private Long Id;

	private Long judgeUserId;

	private Long sponserUserId;

	private String review;


	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getJudgeUserId() {
		return judgeUserId;
	}

	public void setJudgeUserId(Long judgeUserId) {
		this.judgeUserId = judgeUserId;
	}

	public Long getSponserUserId() {
		return sponserUserId;
	}

	public void setSponserUserId(Long sponserUserId) {
		this.sponserUserId = sponserUserId;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
}
