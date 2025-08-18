package com.journal.journalApp.service;

public interface PasswordResetService {

    void initiateReset(String emailOrUsername, String appBaseUrl);

    void resetPassword(String token, String newPassword);
}


