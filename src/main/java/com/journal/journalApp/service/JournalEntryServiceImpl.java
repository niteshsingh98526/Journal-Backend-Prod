package com.journal.journalApp.service;

import com.journal.journalApp.entity.JournalEntry;
import com.journal.journalApp.entity.User;
import com.journal.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryServiceImpl implements JournalEntryService{

    @Autowired
    private UserService userService;

    private JournalEntryRepository journalEntryRepository;

    JournalEntryServiceImpl(JournalEntryRepository journalEntryRepository){
        this.journalEntryRepository=journalEntryRepository;
    }

    @Override
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        }catch (Exception e){
            log.error("e",e);
            throw new RuntimeException("An error occurred while saving the entry." , e);
        }
    }

    @Override
    public void save(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    @Override
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    @Override
    public Optional<JournalEntry> findById(String id) {
        return journalEntryRepository.findById(id);
    }

    @Override
    public boolean deleteById(String id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                this.journalEntryRepository.deleteById(id);
            }
        }catch (Exception e){
            log.error("e",e);
            throw  new RuntimeException("An error occurred by deleting the entry ", e);
        }
        return removed;
    }

}
