package com.suggestions.restaurantname.service;

import java.util.List;

import com.suggestions.restaurantname.model.RestaurantNameSuggestion;
import com.suggestions.restaurantname.model.Session;

public interface RestaurantNameService {
	
	boolean checkSessionExpired(int sessionId);
	
	Session saveSessionDetails(Session session);
	
	String validateSessionDetails(Session session);
	
	String validateEmployeeNo(RestaurantNameSuggestion restaurantNameSuggestion);
	
	List<RestaurantNameSuggestion> findParticipantList(int sessionId);
	
	void saveRestaurantNameSuggestion(RestaurantNameSuggestion restaurantNameSuggestion);
	
	String endSession(RestaurantNameSuggestion restaurantNameSuggestion);
}
