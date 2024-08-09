package com.random.siddhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.random.siddhu.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
   
	User findByUsername(String username);
	 List<User> findByEmail(@Param("email") String email);
	 
	 

}
