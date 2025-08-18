package com.journal.journalApp.service;

import com.journal.journalApp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public void saveNewUser(User user);
    public void saveUser(User user);
    public void saveAdmin(User user);

    public List<User> getAll();

    public User findByName(String userName);

    public Optional<User> findById(String id);

    public void deleteById(String id);

    public List<User> searchByUserName(String q);

    public List<User> searchByEmail(String q);
}
