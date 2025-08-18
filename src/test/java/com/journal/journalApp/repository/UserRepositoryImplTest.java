package com.journal.journalApp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Disabled("tested")
    @Test
    void testSaveNewUser(){
        Assertions.assertNull(userRepository.getUserSorSA());
    }
}
