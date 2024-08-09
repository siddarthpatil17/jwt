package com.random.siddhu.service;

import java.util.ArrayList;

//import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.random.siddhu.model.userDTO;
import com.random.siddhu.repository.UserRepo;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	
//
@Autowired
    private UserRepo userao;

	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.random.siddhu.entity.User user = userao.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}else {
			System.out.println(user);
			return new User(user.getusername(), user.getPassword(),
					new ArrayList<>());
		}
		
	}
	
    public UserRepo save(userDTO use) {
    	com.random.siddhu.entity.User newUser = new com.random.siddhu.entity.User();
		newUser.setusername(use.getUsername());
		newUser.setPassword(bcryptEncoder.encode( use.getPassword()));
		newUser.setActive(use.isActive());
		newUser.setemail(use.getEmail());
		newUser.setPassword(use.getPassword());
		return (UserRepo) userao.save(newUser);
	}

	


}
