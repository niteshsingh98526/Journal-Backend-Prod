package com.journal.journalApp.controller;

import com.journal.journalApp.cache.AppCache;
import com.journal.journalApp.dto.UserDTO;
import com.journal.journalApp.entity.User;
import com.journal.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin API's")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUser(){
        List<User> all = userService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createNormalUser(@RequestBody User user){
        userService.saveNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id){
        Optional<User> user = userService.findById(id);
        return user.<ResponseEntity<?>>map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/by-name/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name){
        User user = userService.findByName(name);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updated){
        Optional<User> existing = userService.findById(id);
        if (existing.isPresent()){
            User toSave = existing.get();
            toSave.setUserName(updated.getUserName() != null ? updated.getUserName() : toSave.getUserName());
            toSave.setEmail(updated.getEmail() != null ? updated.getEmail() : toSave.getEmail());
            if (updated.getPassword() != null && !updated.getPassword().isEmpty()){
                toSave.setPassword(passwordEncoder.encode(updated.getPassword()));
            }
            userService.saveUser(toSave);
            return new ResponseEntity<>(toSave, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search/by-username")
    public ResponseEntity<?> searchByUsername(@RequestParam("q") String q){
        return new ResponseEntity<>(userService.searchByUserName(q), HttpStatus.OK);
    }

    @GetMapping("/search/by-email")
    public ResponseEntity<?> searchByEmail(@RequestParam("q") String q){
        return new ResponseEntity<>(userService.searchByEmail(q), HttpStatus.OK);
    }

    @PutMapping("/user/{id}/roles")
    public ResponseEntity<?> updateRoles(@PathVariable String id, @RequestBody List<String> roles){
        Optional<User> existing = userService.findById(id);
        if (existing.isPresent()){
            User user = existing.get();
            user.setRoles(roles);
            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/clear-app-cache") public void clearAppCache(){
        appCache.init();
    }
}
