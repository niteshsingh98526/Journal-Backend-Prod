package com.journal.journalApp.controller;

import com.journal.journalApp.dto.ForgotPasswordRequest;
import com.journal.journalApp.dto.ResetPasswordRequest;
import com.journal.journalApp.service.PasswordResetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/password")
@CrossOrigin(origins = { "http://localhost:4200", "https://journal-backend-prod-qbom.onrender.com" })
@Tag(name = "Password Reset API's")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest request,
            @RequestHeader(value = "Origin", required = false) String origin) {
        String appBaseUrl = origin != null ? origin : "http://localhost:4200";
        passwordResetService.initiateReset(request.getEmailOrUsername(), appBaseUrl);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
