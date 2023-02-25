package com.security.contests.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class User implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2383308229165154635L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable = false, updatable = false)
	private Long id;
	private String username;
	private String password;
	private String name;
	
	private String phone;
	private boolean enabled=true;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<UserRole> userRoles = new HashSet<>();
	
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private Judge judge;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private Contestant contestant;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private Sponser sponser;
	

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private Wallet wallet;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
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
	 * @return the contestant
	 */
	public Contestant getContestant() {
		return contestant;
	}
	/**
	 * @param contestant the contestant to set
	 */
	public void setContestant(Contestant contestant) {
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
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorites = new HashSet<>();
		userRoles.forEach(ur -> authorites.add(new Authority(ur.getRole().getName())));
		
		return authorites;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @return the judge
	 */
	public Judge getJudge() {
		return judge;
	}
	/**
	 * @param judge the judge to set
	 */
	public void setJudge(Judge judge) {
		this.judge = judge;
	}
	
	
	
}
