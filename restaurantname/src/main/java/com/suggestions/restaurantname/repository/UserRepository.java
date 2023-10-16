package com.suggestions.restaurantname.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suggestions.restaurantname.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByEmpNo(int empNo);
}
