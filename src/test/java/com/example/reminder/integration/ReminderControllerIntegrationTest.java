package com.example.reminder.integration;

import com.example.reminder.dto.ReminderDTO;
import com.example.reminder.mapper.ReminderMapper;
import com.example.reminder.repository.ReminderRepository;
import com.example.reminder.service.ReminderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Date;

import org.hamcrest.CoreMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ReminderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderMapper reminderMapper;

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReminderRepository reminderRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
        registry.add("spring.jpa.show-sql", () -> true);
    }

    @Test
    public void givenReminderDTOList_whenGetAllReminderDTOs_thenReminderDTOsList() throws Exception {


       var listRemindersDTOs = reminderMapper.remindersToReminderDtos(reminderService.getReminders());

        mockMvc.perform(get("/api/v1/list"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(listRemindersDTOs.size())))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void givenReminderPagination_whenGetReminderPagination_thenReminderPagination() throws Exception {

        int total = 2;
        int current = 2;

        Page<ReminderDTO> reminderDTOPage = reminderMapper.remindersToPageDtos
                (reminderService.getReminderPagination(total, current, null));

        mockMvc.perform(get("/api/v1/list?total=2&current=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(reminderDTOPage)))

                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void givenReminderPaginationSort_whenGetReminderPaginationSort_thenReminderPaginationSort() throws Exception {

        int title = 1;
        int date = 1;
        String time = "id";

        Page<ReminderDTO> page =
                reminderMapper.remindersToSortDtos(reminderService.getReminderPagination(title, date, time));

        mockMvc.perform(get("/api/v1/list?title=1&date=1&time=id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(page)))

                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void givenCreateReminder_whenCreateReminder_thenReminder() throws Exception {

        ReminderDTO reminderDTO = new ReminderDTO();
        reminderDTO.setId(1);
        reminderDTO.setTitle("Reminder Title");
        reminderDTO.setRemind(new Date(2024 - 12 - 28 - 22 - 10 - 55));
        reminderDTO.setDescription("This is a reminder");
        reminderDTO.setUserId(1);

        var request = reminderService.createReminder(reminderMapper.reminderDtoToReminder(reminderDTO));

        mockMvc.perform(post("/api/v1/reminder/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void givenUpdateReminder_whenUpdateReminder_thenReminder() throws Exception {

        ReminderDTO reminderDTO = new ReminderDTO();
        reminderDTO.setId(1);
        reminderDTO.setTitle("Reminder Title");
        reminderDTO.setRemind(new Date(2024 - 12 - 28 - 22 - 10 - 55));
        reminderDTO.setDescription("This is a reminder");
        reminderDTO.setUserId(1);

        ReminderDTO updateReminderDTO = new ReminderDTO();
        updateReminderDTO.setId(3);
        updateReminderDTO.setTitle("Reminder Title3");
        updateReminderDTO.setRemind(new Date(2025 - 01 - 03 - 03 - 03 - 55));
        updateReminderDTO.setDescription("This is a reminder3");
        updateReminderDTO.setUserId(3);

        reminderService.updateReminder(reminderDTO.getId(), reminderMapper.reminderDtoToReminder(reminderDTO));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/{id}", reminderDTO.getId())
                        .content(objectMapper.writeValueAsString(updateReminderDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))

                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void givenDeleteReminder_whenDeleteReminder_thenReminder() throws Exception {

        ReminderDTO reminderDTO = new ReminderDTO();
        reminderDTO.setId(1);
        reminderDTO.setTitle("Reminder Title");
        reminderDTO.setRemind(new Date(2024 - 12 - 28 - 22 - 10 - 55));
        reminderDTO.setDescription("This is a reminder");
        reminderDTO.setUserId(1);

        reminderService.createReminder(reminderMapper.reminderDtoToReminder(reminderDTO));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/reminder/delete/{id}", reminderDTO.getId()))

                .andExpect(status().isOk())
                .andDo(print());
    }
}
//integration test
