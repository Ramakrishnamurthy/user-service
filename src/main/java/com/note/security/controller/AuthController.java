package com.note.security.controller;



import com.note.security.dao.*;
import com.note.security.dao.model.*;
import com.note.security.dto.*;
import com.note.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
     * @param loginUser
     * @return
     */
    @PostMapping("/register")
    public String registerUser(@RequestBody LoginUser loginUser) {
        if (userRepository.findByUsername(loginUser.getUsername()).isPresent()) {
            return "Username already exists!";
        }
        loginUser.setPassword(passwordEncoder.encode(loginUser.getPassword()));
        userRepository.save(loginUser);
        return "LoginUser registered successfully!";
    }


    /**
     * authenticates the user
     * @param authenticationRequest
     * @return
     */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
