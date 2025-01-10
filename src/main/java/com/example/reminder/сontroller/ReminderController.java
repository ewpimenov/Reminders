package com.example.reminder.сontroller;

import com.example.reminder.dto.ReminderDTO;
import com.example.reminder.mapper.ReminderMapper;
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

    private final ReminderMapper reminderMapper;

    public ReminderController(ReminderService reminderService, ReminderMapper reminderMapper) {
        this.reminderService = reminderService;
        this.reminderMapper = reminderMapper;
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<ReminderDTO>> getAll() {
        return new ResponseEntity<>(reminderMapper.remindersToReminderDtos(reminderService.getReminders()),
                HttpStatus.OK);
    }

    @GetMapping("/list/{total}/{current}")
    public List<ReminderDTO> getReminders(@PathVariable Integer total, @PathVariable Integer current) {
        Page<ReminderDTO> page =
                reminderMapper.remindersToPageDtos(reminderService.getReminderPagination(total, current, null));
        return page.getContent();
    }

    @GetMapping("/sort/{title}/{date}/{time}")
    public List<ReminderDTO> getReminders(@PathVariable Integer title, @PathVariable Integer date,
                                          @PathVariable String time) {
        Page<ReminderDTO> page =
                reminderMapper.remindersToSortDtos(reminderService.getReminderPagination(title, date, time));
        return page.getContent();
    }

    @PostMapping(value = "/reminder/create")
    public ResponseEntity<ReminderDTO> createReminder(@RequestBody ReminderDTO reminderDTO) {
        reminderService.createReminder(reminderMapper.reminderDtoToReminder(reminderDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(reminderDTO);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<ReminderDTO> updateReminder(@PathVariable Integer id, @RequestBody ReminderDTO reminderDTO) {
        reminderService.updateReminder(id, reminderMapper.reminderDtoToReminder(reminderDTO));
        return ResponseEntity.status(HttpStatus.OK).body(reminderDTO);
    }

    @DeleteMapping(value = "/reminder/delete/{id}")
    public ResponseEntity<ReminderDTO> deleteReminder(@PathVariable Integer id) {
        reminderService.deleteReminder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}