package com.example.reminder.service;

import com.example.reminder.bot.ReminderBot;
import com.example.reminder.config.ReminderBotConfig;
import com.example.reminder.model.Reminder;
import com.example.reminder.model.User;
import com.example.reminder.repository.ReminderRepository;
import com.example.reminder.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerService {

    private final JavaMailSender mailSender;
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final ReminderBot reminderBot;

    @Value("${bot.chatId}")
    String chatId;

    public void sendMessageToEmail(String to, String subject, String body, String from) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(from);
        mailSender.send(message);
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void sendMessageToEmailAndTelegram() {

        List<Reminder> reminders = reminderRepository.findAll();
        List<User> users = userRepository.findAll();

        for (Reminder reminder : reminders) {
            for (User user : users) {
                sendMessageToEmail(
                        user.getEmail(),
                        user.getUsername(),
                        reminder.getTitle(),
                        "spring.mail.username");

                reminderBot.sendMessage(
                        Long.valueOf(chatId),
                        reminder.getTitle());
            }
        }
    }
}
