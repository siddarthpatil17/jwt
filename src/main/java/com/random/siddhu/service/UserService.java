package com.random.siddhu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.random.siddhu.entity.User;
import com.random.siddhu.model.ActiveUserDTO;
import com.random.siddhu.repository.UserRepo;




@Service
public class UserService {

	@Autowired
	UserRepo userRepo;
	
	
	public User save(User user) {
		
		return userRepo.save(user);
		
	}

    public List<User> getAllUsers() {
        return  userRepo.findAll();
    }

    public User getUserById(int id) {
        return userRepo.findById(id).orElse(null);
    }
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }

    public boolean emailExists(String email) {
        List<User> users = userRepo.findByEmail(email);
        return !users.isEmpty();
    }
    
    public List<ActiveUserDTO> getActiveUsers() {
        return userRepo.findAll().stream()
            .filter(User::isActive)
            .map(user -> new ActiveUserDTO(user.getUserId(), user.getEmail(), user.getCompany_id()))
            .collect(Collectors.toList());
    }


    
    public User getUserByEmail(String email) {
        return userRepo.findByUsername(email);
    }
    public long countActiveUsers() {
        return getActiveUsers().size();
    }
    
	
}
