package com.example.reminder.mapper;

import com.example.reminder.dto.ReminderDTO;
import com.example.reminder.model.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReminderMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "remind", target = "remind"),
            @Mapping(source = "userId", target = "userId"),
    })
    ReminderDTO reminderToReminderDto(Reminder reminder);

    Reminder reminderDtoToReminder(ReminderDTO reminderDTO);

    List<ReminderDTO> remindersToReminderDtos(List<Reminder> reminders);

    default Page<ReminderDTO> remindersToPageDtos(Page<Reminder> reminders) {
        return reminders.map(this::reminderToReminderDto);
    }

    default Page<ReminderDTO> remindersToSortDtos(Page<Reminder> reminderPagination) {
        return reminderPagination.map(this::reminderToReminderDto);
    }
}
