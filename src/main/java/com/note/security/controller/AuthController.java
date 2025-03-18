package com.note.security.controller;

import com.note.security.dao.LoginUserRepository;
import com.note.security.dao.model.LoginUser;
import com.note.security.dto.AuthenticationRequest;
import com.note.security.dto.AuthenticationResponse;
import com.note.security.service.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private LoginUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * registers the user.
     *
     * @param loginUser
     * @return
     */
    @PostMapping("/register")
    public String registerUser(@RequestBody LoginUser loginUser) {
        logger.info("Attempting to register user with username: {}", loginUser.getUsername());

        if (userRepository.findByUsername(loginUser.getUsername()).isPresent()) {
            logger.warn("Username '{}' already exists.", loginUser.getUsername());
            return "Username already exists!";
        }

        loginUser.setPassword(passwordEncoder.encode(loginUser.getPassword()));
        userRepository.save(loginUser);

        logger.info("User '{}' registered successfully.", loginUser.getUsername());
        return "LoginUser registered successfully!";
    }


    /**
     * authenticates the user
     *
     * @param authenticationRequest
     * @return
     */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("Attempting to authenticate user with username: {}", authenticationRequest.getUsername());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            logger.info("Authentication successful for user '{}'.", authenticationRequest.getUsername());
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user '{}'.", authenticationRequest.getUsername(), e);
            throw new RuntimeException("Authentication failed");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        logger.info("Generated JWT for user '{}'.", authenticationRequest.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
