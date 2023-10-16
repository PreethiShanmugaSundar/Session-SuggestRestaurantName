package com.suggestions.restaurantname.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.suggestions.restaurantname.model.RestaurantNameSuggestion;

@Repository
public interface RestaurantNameSuggestionRepository extends CrudRepository<RestaurantNameSuggestion, Long>{
	List<RestaurantNameSuggestion> findBySessionId(int sessionId);
	RestaurantNameSuggestion findBySessionIdAndEmpNo(int sessionId, int empNo);
	List<RestaurantNameSuggestion> findRestaurantNameBySessionId(int sessionId);
}
