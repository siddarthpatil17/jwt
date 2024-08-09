package com.random.siddhu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.random.siddhu.config.JwtTokenUtil;
import com.random.siddhu.entity.RefreshToken;
import com.random.siddhu.entity.User;
import com.random.siddhu.model.ActiveUserDTO;
//import com.random.siddhu.model.ActiveUsersResponse;
import com.random.siddhu.model.JwtRequest;
import com.random.siddhu.model.JwtResponse;
import com.random.siddhu.model.RefreshTokenRequest;
import com.random.siddhu.model.TokenRefreshResponse;
import com.random.siddhu.model.TokensRequest;
import com.random.siddhu.model.userDTO;
import com.random.siddhu.service.JwtUserDetailsService;
import com.random.siddhu.service.RefreshTokenService;
import com.random.siddhu.service.SessionService;
import com.random.siddhu.service.UserService;

@RestController
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        ResponseEntity<?> resp = null;
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            User user = userService.getUserByEmail(authenticationRequest.getUsername());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Save or update session
            sessionService.createOrUpdateSession(userDetails.getUsername(), token, user.getCompany_id());

            // Generate refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserId());

            resp = ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return resp;
    }

//    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
//    public ResponseEntity<?> refreshAuthenticationToken(@RequestBody RefreshTokenRequest refreshTokenRequest, HttpServletRequest req) {
//        String refreshToken = refreshTokenRequest.getRefreshToken();
//        Optional<RefreshToken> token = refreshTokenService.findByToken(refreshToken);
//        if (token.isPresent()) {
//        	
//        	
//            //final UserDetails userDetails = userDetailsService.loadUserByUsername(getUsernameByUserId(token.get().getUserId()));
//            //final String newToken = jwtTokenUtil.generateToken(userDetails);
//        	RefreshToken refreshToken1 = refreshTokenService.verifyExpiration(token.get());
//        	
//        	User user = refreshToken1.getUser();
//        	 String token1 = jwtTokenUtil.doGenerateToken(user.getEmail());
//        	 Long accessExpiry = jwtTokenUtil.getExpirationDateFromToken(token1).getTime();
//        	 TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(token1, refreshToken);
//
//            sessionService.refreshSession(token1);
//
//            return ResponseEntity.ok(new JwtResponse(token1, refreshToken));
//        } else {
//        	System.out.println("hiii... ");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
//        }
//    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public ResponseEntity<?> refreshAuthenticationToken(@RequestBody RefreshTokenRequest refreshTokenRequest, HttpServletRequest req) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        Optional<RefreshToken> token = refreshTokenService.findByToken(refreshToken);
        if (token.isPresent()) {
            try {
                RefreshToken refreshToken1 = refreshTokenService.verifyExpiration(token.get());
                User user = refreshToken1.getUser();
                String newAccessToken = jwtTokenUtil.doGenerateToken(user.getEmail());
                Long accessExpiry = jwtTokenUtil.getExpirationDateFromToken(newAccessToken).getTime();

                // Update the session with the new access token and its expiration time
                sessionService.updateSessionExpiration(user.getUserId(), newAccessToken, accessExpiry);

                TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(newAccessToken, refreshToken);
                return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken));
            } catch (Exception e) {
                System.out.println("Error refreshing token: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }



    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody userDTO user) throws Exception {
        if (userService.emailExists(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email ID already exists");
        }
        User userNew = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userNew.setCompany_id(user.getCompany_id());
        userNew.setusername(user.getUsername());
        userNew.setemail(user.getEmail());
        userNew.setPhoneNo(user.getPhoneNo());
        userNew.setActive(user.isActive());
        userNew.setPassword(encoder.encode(user.getPassword()));

        User savedUser = userService.save(userNew);
        return ResponseEntity.ok(savedUser);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            User user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody userDTO user) {
        User existingUser = userService.getUserById(id);
        if (existingUser != null) {
            existingUser.setusername(user.getUsername());
            existingUser.setemail(user.getEmail());
            existingUser.setPhoneNo(user.getPhoneNo());
            existingUser.setActive(user.isActive());
            existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            return ResponseEntity.ok(userService.updateUser(existingUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private int getUserIdByUsername(String username) {
        // Implement logic to get user ID by username
        return 1; // Placeholder
    }

    private String getUsernameByUserId(int userId) {
        // Implement logic to get username by user ID
        return "username"; // Placeholder
    }
    
    

    @GetMapping("/activeUsers")
    public ResponseEntity<?> getActiveUsers() {
        List<ActiveUserDTO> activeUsers = userService.getActiveUsers();
        long activeUserCount = userService.countActiveUsers();

        Map<String, Object> response = new HashMap<>();
        response.put("activeUserCount", activeUserCount);
        response.put("activeUsers", activeUsers);

        return ResponseEntity.ok(response);
    }
}
