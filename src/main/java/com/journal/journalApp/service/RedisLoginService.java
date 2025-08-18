package com.journal.journalApp.service;

import com.journal.journalApp.repository.UserRepository;
import com.journal.journalApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    public String login(String userName) {

        String existingToken = (String) redisTemplate.opsForValue().get(userName);
        if (existingToken != null && jwtUtil.validateToken(existingToken)) {
            return existingToken;  // Return the existing valid token from Redis
        }

        // Authenticate from the database
        UserDetails user = userDetailsService.loadUserByUsername(userName);
        if (user != null ) {
            // Generate new JWT token
            String jwtToken = jwtUtil.generateToken(user.getUsername());
            log.info("Storing token in Redis for user: {}", userName);

            try {
                //Save generated token in redis for 1 hr
                redisTemplate.opsForValue().set(userName, jwtToken, 60 * 60, TimeUnit.SECONDS);
                log.info("Token stored successfully in Redis for user: {}", userName);
            } catch (Exception e) {
                log.error("Failed to store token in Redis for user: {}", userName, e);
            }

            return jwtToken;
        }
        throw new RuntimeException("token is not there!");
    }
}
