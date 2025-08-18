package com.journal.journalApp.service;

import com.journal.journalApp.entity.PasswordResetToken;
import com.journal.journalApp.entity.User;
import com.journal.journalApp.repository.PasswordResetTokenRepository;
import com.journal.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initiateReset(String emailOrUsername, String appBaseUrl) {
        User user = userRepository.findByEmail(emailOrUsername);
        if (user == null) {
            user = userRepository.findByUserName(emailOrUsername);
        }
        if (user == null) {
            log.warn("Password reset requested for non-existent user: {}", emailOrUsername);
            return; // don't reveal existence
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setUserId(user.getId());
        prt.setExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
        prt.setUsed(false);
        tokenRepository.save(prt);

        String resetLink = appBaseUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> prtr = tokenRepository.findByToken(token);
        if (prtr.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        PasswordResetToken prt = prtr.get();
        if (prt.isUsed() || prt.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Expired or used token");
        }
        User user = userRepository.findById(prt.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        prt.setUsed(true);
        tokenRepository.save(prt);
    }
}


