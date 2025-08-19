package com.journal.journalApp.controller;

import com.journal.journalApp.api.response.WeatherResponse;
import com.journal.journalApp.dto.UserDTO;
import com.journal.journalApp.entity.User;
import com.journal.journalApp.repository.UserRepository;
import com.journal.journalApp.service.UserService;
import com.journal.journalApp.service.WeatherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
@Tag(name = "User API's", description = "Read, Update, Delete")
public class UserController {

    Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;


    @GetMapping("/allUser")
    public ResponseEntity<List> getAllUser() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("{name}")
    public ResponseEntity<User> getUserByUserName(@PathVariable String name) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User byName = userService.findByName(name);
            return new ResponseEntity<>(byName, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("error");
        }
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByName(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // allow user to delete self or admin to delete via admin controller; here only self-delete
        User current = userService.findByName(authentication.getName());
        if (current != null && id.equals(current.getId())){
            userService.deleteById(id);
            return new ResponseEntity<>("success",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByName(userName);
        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>("update success", HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String greeting = "";
        if (weatherResponse != null) {
            greeting = ", Weather feels like " + weatherResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }
}
