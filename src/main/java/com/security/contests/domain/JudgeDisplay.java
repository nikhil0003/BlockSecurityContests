package com.security.contests.domain;

public class JudgeDisplay {

	
	public boolean judgeSno =false;
   
	public String  judgeName;
	
	public Long judgeUserId;

	/**
	 * @return the judgeName
	 */
	public final String getJudgeName() {
		return judgeName;
	}

	/**
	 * @param judgeName the judgeName to set
	 */
	public final void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}

	/**
	 * @return the judgeUserId
	 */
	public final Long getJudgeUserId() {
		return judgeUserId;
	}

	/**
	 * @param judgeUserId the judgeUserId to set
	 */
	public final void setJudgeUserId(Long judgeUserId) {
		this.judgeUserId = judgeUserId;
	}

	/**
	 * @return the judgeSno
	 */
	public final boolean isJudgeSno() {
		return judgeSno;
	}

	/**
	 * @param judgeSno the judgeSno to set
	 */
	public final void setJudgeSno(boolean judgeSno) {
		this.judgeSno = judgeSno;
	}
	
	
	
	
}
