package com.security.contests.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CreateConstestModel implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4815692903514925536L;
	
	public Long id;

	public String firstname;
	
	public ArrayList<JudgeDisplay> jdlist;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date startDate;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date endDate;
    
    

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
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the jdlist
	 */
	public ArrayList<JudgeDisplay> getJdlist() {
		return jdlist;
	}

	/**
	 * @param jdlist the jdlist to set
	 */
	public void setJdlist(ArrayList<JudgeDisplay> jdlist) {
		this.jdlist = jdlist;
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
	

	
	
	
}
