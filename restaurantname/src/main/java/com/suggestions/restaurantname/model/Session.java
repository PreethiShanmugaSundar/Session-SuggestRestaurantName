package com.suggestions.restaurantname.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "sessions")

public class Session {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sessionId;
	@Column(name = "createdBy")
	private String createdBy;
	@Column(name = "sessionStartDate")
	private LocalDateTime sessionStartDate;
	@Column(name = "sessionEndDate")
	private LocalDateTime sessionEndDate;
	@Column(name = "participantNames")
	private String participantNames;
	private String sessionUrl;
	private List<String> employeeNoList;
	private String restaurantNameChosen;
	
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getSessionStartDate() {
		return sessionStartDate;
	}
	public void setSessionStartDate(LocalDateTime sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}
	public LocalDateTime getSessionEndDate() {
		return sessionEndDate;
	}
	public void setSessionEndDate(LocalDateTime sessionEndDate) {
		this.sessionEndDate = sessionEndDate;
	}
	public String getParticipantNames() {
		return participantNames;
	}
	public void setParticipantNames(String participantNames) {
		this.participantNames = participantNames;
	}
	public String getSessionUrl() {
		return sessionUrl;
	}
	public void setSessionUrl(String sessionUrl) {
		this.sessionUrl = sessionUrl;
	}
	public List<String> getEmployeeNoList() {
		return employeeNoList;
	}
	public void setEmployeeNoList(List<String> employeeNoList) {
		this.employeeNoList = employeeNoList;
	}
	public String getRestaurantNameChosen() {
		return restaurantNameChosen;
	}
	public void setRestaurantNameChosen(String restaurantNameChosen) {
		this.restaurantNameChosen = restaurantNameChosen;
	}
	
	
	
}
