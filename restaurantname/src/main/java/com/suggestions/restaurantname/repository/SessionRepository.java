package com.suggestions.restaurantname.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.suggestions.restaurantname.model.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
}
