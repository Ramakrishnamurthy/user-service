package com.note.security.service;

import com.note.security.dao.*;
import com.note.security.dao.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUser loginLoginUserEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("LoginUser not found"));

        return User.builder()
                .username(loginLoginUserEntity.getUsername())
                .password(loginLoginUserEntity.getPassword())
                .build();
    }
}