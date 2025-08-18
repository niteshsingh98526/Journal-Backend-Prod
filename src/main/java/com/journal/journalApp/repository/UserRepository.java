package com.journal.journalApp.repository;

import com.journal.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUserName(String userName);

    void deleteByUserName(String userName);

    List<User> findByUserNameContainingIgnoreCase(String userName);

    List<User> findByEmailContainingIgnoreCase(String email);

    User findByEmail(String email);

    boolean existsByUserName(String userName);
}
