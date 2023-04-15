package com.security.contests.domain;

public class JudgeGradeDispaly {

	public Long judgeId;

	public String judgeName;

	public Long contestantId;

	public String dataArea;

	public Long gradeValue;
	
	public Long contestId;

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

	public String getJudgeName() {
		return judgeName;
	}

	public void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
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
	
	

}
