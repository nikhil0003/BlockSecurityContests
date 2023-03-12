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
public class Contestant {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id", nullable = false, updatable = false)
	private Long Id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="contestId")
	private Contest contest;

	
	@OneToOne(cascade = CascadeType.ALL)
	private User user;
	
	@Column(name="dataArea")
	private String dataArea;
	
	@Column(name="grade")
	private Long grade;


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
	 * @return the contest
	 */
	public Contest getContest() {
		return contest;
	}


	/**
	 * @param contest the contest to set
	 */
	public void setContest(Contest contest) {
		this.contest = contest;
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
	 * @return the dataArea
	 */
	public String getDataArea() {
		return dataArea;
	}


	/**
	 * @param dataArea the dataArea to set
	 */
	public void setDataArea(String dataArea) {
		this.dataArea = dataArea;
	}


	/**
	 * @return the grade
	 */
	public Long getGrade() {
		return grade;
	}


	/**
	 * @param grade the grade to set
	 */
	public void setGrade(Long grade) {
		this.grade = grade;
	}
	
	

}
