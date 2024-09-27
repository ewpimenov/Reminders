package com.example.reminder.сontroller;

import com.example.reminder.model.Reminder;
import com.example.reminder.service.ReminderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }


    @GetMapping(value = "/list")
    public ResponseEntity<List<Reminder>> getAll() {
        return new ResponseEntity<>(reminderService.getReminders(), HttpStatus.OK);
    }

    @GetMapping("/list/{total}/{current}")
    public List <Reminder> getReminders(@PathVariable Integer total, @PathVariable Integer current) {
        Page<Reminder> page = reminderService.getReminderPagination(total, current, null);
        return page.getContent();
    }

   @GetMapping("/sort/{title}/{date}/{time}")
   public List <Reminder> getReminders(@PathVariable Integer title, @PathVariable Integer date,
                                       @PathVariable String time) {
       Page<Reminder> page = reminderService.getReminderPagination(title, date, time);
       return page.getContent();
   }

    @PostMapping(value = "/reminder/create")
    public ResponseEntity<Reminder> createReminder(@RequestBody Reminder reminder) {
        Reminder newReminder = reminderService.createReminder(reminder);
        return new ResponseEntity<>(newReminder, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable Integer id, @RequestBody Reminder reminder) {
        reminderService.updateReminder(id, reminder);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/reminder/delete/{id}")
    public ResponseEntity<Reminder> deleteReminder(@PathVariable Integer id) {
        reminderService.deleteReminder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}