package com.note.security.dao;

import com.note.security.dao.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {
    // Define custom queries if needed

    Optional<LoginUser> findByUsername(String username);}