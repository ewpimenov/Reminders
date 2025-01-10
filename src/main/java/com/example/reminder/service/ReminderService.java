package com.example.reminder.service;

import com.example.reminder.model.Reminder;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ReminderService {

    Reminder createReminder(Reminder reminder);

    List<Reminder> getReminders();

    Reminder updateReminder(Integer id, Reminder reminder);

    void deleteReminder(Integer id);

    Page<Reminder> getReminderPagination(Integer total, Integer current, String sort);
}
