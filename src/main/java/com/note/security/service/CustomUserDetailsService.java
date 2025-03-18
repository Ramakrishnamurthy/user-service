package com.note.security.service;

import com.note.security.dao.LoginUserRepository;
import com.note.security.dao.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginUserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);

        LoginUser loginLoginUserEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("LoginUser not found with username: {}", username);
                    return new UsernameNotFoundException("LoginUser not found");
                });

        logger.info("Found user with username: {}", loginLoginUserEntity.getUsername());

        UserDetails userDetails = User.builder()
                .username(loginLoginUserEntity.getUsername())
                .password(loginLoginUserEntity.getPassword())
                .build();

        logger.info("Loaded user details for username: {}", userDetails.getUsername());

        return userDetails;
    }
}