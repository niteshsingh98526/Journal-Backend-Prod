package com.journal.journalApp.controller;

import com.journal.journalApp.dto.JwtResponse;
import com.journal.journalApp.dto.UserDTO;
import com.journal.journalApp.entity.User;
import com.journal.journalApp.service.RedisLoginService;
import com.journal.journalApp.service.RedisService;
import com.journal.journalApp.service.UserDetailsServiceImpl;
import com.journal.journalApp.service.UserService;
import com.journal.journalApp.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@Tag(name = "Public API's")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisLoginService redisLoginService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody UserDTO user){
        try {
            User userDTO = new User();
            userDTO.setEmail(user.getEmail());
            userDTO.setUserName(user.getUserName());
            userDTO.setPassword(user.getPassword());
            userService.saveNewUser(userDTO);
            return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<User> createAdmin(@RequestBody UserDTO user){
        try {
            User userDTO = new User();
            userDTO.setEmail(user.getEmail());
            userDTO.setUserName(user.getUserName());
            userDTO.setPassword(user.getPassword());
            userService.saveAdmin(userDTO);
            return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody JwtResponse response) {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(response.getUserName(), response.getPassword()));
//            UserDetails userDetails = userDetailsService.loadUserByUsername(response.getUserName());
//            String redisToken = redisLoginService.login(userDetails.getUsername());
//            return new ResponseEntity<>(redisToken, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Exception occurred while createauthenticationToken", e);
//            return new ResponseEntity<>("Incorrect username and password", HttpStatus.BAD_REQUEST);
//        }
//    }

    
    @GetMapping("/health")
    public String helthCheck(){
        return "OK";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody JwtResponse request) {
        try {
            // Authentication process
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
            String redisToken = redisLoginService.login(userDetails.getUsername());

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("token", redisToken);  // Return token inside an object
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while creating authenticationToken", e);
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }

}
}
