package com.journal.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTests {

    @Autowired
    private EmailService emailService;


    @Disabled
    @Test
    void testSendMail(){
        emailService.sendMail("vk129579@gmail.com",
                "Testing Java Mail Sender",
                "Hi, beta kaise ho!");
    }
}
