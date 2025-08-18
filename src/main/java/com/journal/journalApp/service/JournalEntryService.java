package com.journal.journalApp.service;

import com.journal.journalApp.entity.JournalEntry;

import java.util.List;
import java.util.Optional;

public interface JournalEntryService {

    void saveEntry(JournalEntry journalEntry, String userName);

    void save(JournalEntry journalEntry);

    List<JournalEntry> getAll();

    Optional<JournalEntry> findById(String id);

    boolean deleteById(String id, String userName);
}
