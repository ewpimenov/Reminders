package com.example.reminder.service;

import com.example.reminder.model.Reminder;
import com.example.reminder.repository.ReminderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReminderServiceImpl implements ReminderService {

    ReminderRepository reminderRepository;

    public ReminderServiceImpl(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Override
    public List<Reminder> getReminders() {
        return reminderRepository.findAll();
    }

    @Override
    public Page<Reminder> getReminderPagination(Integer total, Integer current, String sort) {
        Pageable pageable = null;
        if (sort != null) {
            // with sorting
            pageable = PageRequest.of(total, current, Sort.Direction.ASC, sort);
        } else {
            // without sorting
            pageable = PageRequest.of(total, current);
        }
        return reminderRepository.findAll(pageable);
    }


    @Override
    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @Override
    public Reminder updateReminder(Integer id, Reminder reminder) {
      return reminderRepository.saveAndFlush(reminder);
    }

    @Override
    public void deleteReminder(Integer id) {
        reminderRepository.deleteById(id);
    }
}
