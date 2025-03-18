package com.note.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");
        http
                .csrf(csrf -> {
                    logger.info("Disabling CSRF");
                    csrf.disable();
                })  // Disable CSRF
                .authorizeHttpRequests(auth -> {
                    logger.info("Configuring authorization for HTTP requests");
                    auth
                            .requestMatchers("/auth/**", "/*").permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    logger.info("Configuring session management");
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .formLogin()
                .and()
                .authenticationProvider(authenticationProvider());
        logger.info("Adding JWT request filter before UsernamePasswordAuthenticationFilter");
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("Building security filter chain");
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        logger.info("Creating authentication provider");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        logger.info("Setting user details service for authentication provider");
        authProvider.setUserDetailsService(userDetailsService());
        logger.info("Setting password encoder for authentication provider");
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.info("Creating authentication manager");
        return config.getAuthenticationManager();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        logger.info("Creating user details service");
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating password encoder");
        return new BCryptPasswordEncoder();
    }
}