package com.security.contests.domain;

public class Grade {

	public Long id;
	
	public Long contestantId;
	
	public Long userId;
	
	public Long contestId;
	
	public Long judgeId;
	
	public Long gradeValue;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the contestantId
	 */
	public Long getContestantId() {
		return contestantId;
	}

	/**
	 * @param contestantId the contestantId to set
	 */
	public void setContestantId(Long contestantId) {
		this.contestantId = contestantId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the contestId
	 */
	public Long getContestId() {
		return contestId;
	}

	/**
	 * @param contestId the contestId to set
	 */
	public void setContestId(Long contestId) {
		this.contestId = contestId;
	}

	/**
	 * @return the judgeId
	 */
	public Long getJudgeId() {
		return judgeId;
	}

	/**
	 * @param judgeId the judgeId to set
	 */
	public void setJudgeId(Long judgeId) {
		this.judgeId = judgeId;
	}

	/**
	 * @return the gradeValue
	 */
	public Long getGradeValue() {
		return gradeValue;
	}

	/**
	 * @param gradeValue the gradeValue to set
	 */
	public void setGradeValue(Long gradeValue) {
		this.gradeValue = gradeValue;
	}
	
	
}
