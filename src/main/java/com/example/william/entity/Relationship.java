package com.example.william.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "relationship")
public class Relationship {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rid;
	
	@Column(name = "sponsor")
	private Integer sponsor;
	
	@Column(name = "accepter")
	private Integer accepter;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "intimacy_level")
	private Integer intimacyLevel;
	

	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public Integer getSponsor() {
		return sponsor;
	}
	public void setSponsor(Integer sponsor) {
		this.sponsor = sponsor;
	}
	public Integer getAccepter() {
		return accepter;
	}
	public void setAccepter(Integer accepter) {
		this.accepter = accepter;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getIntimacyLevel() {
		return intimacyLevel;
	}
	public void setIntimacyLevel(Integer intimacyLevel) {
		this.intimacyLevel = intimacyLevel;
	}

}
