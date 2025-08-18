package com.journal.journalApp.service;

import com.journal.journalApp.scheduler.UserScheduler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserSchedulersTest {

    @Autowired
    private UserScheduler userScheduler;

    @Disabled("tested")
    @Test
    void testFetchUsersAndSendSaMail(){
        userScheduler.fetchUsersAndSendSaMail();
    }
}
