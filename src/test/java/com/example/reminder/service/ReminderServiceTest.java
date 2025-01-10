package com.example.reminder.service;

import com.example.reminder.model.Reminder;
import com.example.reminder.repository.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    @InjectMocks
    private ReminderServiceImpl reminderService;

    @Mock
    private ReminderRepository reminderRepository;

    private Reminder reminder;

    @BeforeEach
    public void setup() {

        reminder = new Reminder();
        reminder.setId(1);
        reminder.setTitle("Reminder Title");
        reminder.setRemind(new Date(2024 - 12 - 28 - 22 - 10 - 54));
        reminder.setDescription("This is a reminder");
        reminder.setUserId(1);
    }

    @Test
    void givenReminderList_whenGetAllReminders_thenReturnRemindersList() {

        Reminder reminder1 = new Reminder();
        reminder1.setId(2);
        reminder1.setTitle("Reminder Title2");
        reminder1.setRemind(new Date(2024 - 12 - 28 - 22 - 10 - 55));
        reminder1.setDescription("This is a reminder2");
        reminder1.setUserId(2);

        given(reminderRepository.findAll()).willReturn(List.of(reminder, reminder1));

        List<Reminder> reminderList = reminderService.getReminders();

        List<Reminder> reminderListExpected = new ArrayList<>();
        reminderListExpected.add(reminder);
        reminderListExpected.add(reminder1);

        assertThat(reminderList).isNotNull();
        assertThat(reminderList.size()).isEqualTo(reminderListExpected.size());
    }

    @Test
    public void givenRemider_whenSaveReminder_thenReturnReminder() {

        given(reminderRepository.save(reminder)).willReturn(reminder);

        Reminder savedReminder = reminderService.createReminder(reminder);

        assertThat(savedReminder).isNotNull();
    }

    @Test
    public void givenReminder_whenUpdateReminder_thenReturnReminder() {

        given(reminderRepository.saveAndFlush(reminder)).willReturn(reminder);

        reminder.setId(3);
        reminder.setTitle("Reminder Title3");
        reminder.setRemind(new Date(2024 - 12 - 28 - 22 - 10 - 54));
        reminder.setDescription("This is a reminder3");
        reminder.setUserId(3);

        reminderService.updateReminder(reminder.getId(), reminder);

        assertThat(reminder.getId()).isEqualTo(3);
        assertThat(reminder.getTitle()).isEqualTo("Reminder Title3");
        assertThat(reminder.getRemind()).isNotNull();
        assertThat(reminder.getDescription()).isEqualTo("This is a reminder3");
        assertThat(reminder.getUserId()).isEqualTo(3);
    }

    @Test
    public void givenReminderId_whenDeleteReminder_thenNothing() {

        reminder.setId(4);
        reminder.setTitle("Reminder Title4");
        reminder.setRemind(new Date(2024 - 10 - 10 - 10 - 10 - 55));
        reminder.setDescription("This is a reminder4");
        reminder.setUserId(4);

        reminderRepository.deleteById(reminder.getId());

        assertThat(reminderRepository.existsById(reminder.getId())).isFalse();
    }

    @Test
    public void givenReminderPage_whenGetReminderPage_thenReturnRemindersPage() {

        Pageable pageable = PageRequest.of(1, 2, Sort.by("title"));

        reminderRepository.findAll(pageable);

        assertThat(pageable).isNotNull();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(2);
        assertThat(pageable.getSort()).isEqualTo(Sort.by("title"));
    }
}
