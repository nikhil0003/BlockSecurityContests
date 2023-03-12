package com.security.contests.domain;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Contest {
	

	private static final long serialVersionUID = -8330820929165154635L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id", nullable = false, updatable = false)
	private Long Id;
	
	private String name;
	
	private Date startDate;
	
	private Date endDate;

	private Long sponserAmount;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
	private List<Judge> JudgeList;

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
	private List<Contestant> contestant;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "contest")
	private Sponser sponser;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the judgeList
	 */
	public List<Judge> getJudgeList() {
		return JudgeList;
	}


	/**
	 * @param judgeList the judgeList to set
	 */
	public void setJudgeList(List<Judge> judgeList) {
		JudgeList = judgeList;
	}


	
	/**
	 * @return the contestant
	 */
	public List<Contestant> getContestant() {
		return contestant;
	}


	/**
	 * @param contestant the contestant to set
	 */
	public void setContestant(List<Contestant> contestant) {
		this.contestant = contestant;
	}


	/**
	 * @return the sponser
	 */
	public Sponser getSponser() {
		return sponser;
	}


	/**
	 * @param sponser the sponser to set
	 */
	public void setSponser(Sponser sponser) {
		this.sponser = sponser;
	}


	public Long getSponserAmount() {
		return sponserAmount;
	}

	public void setSponserAmount(Long sponserAmount) {
		this.sponserAmount = sponserAmount;
	}
}
