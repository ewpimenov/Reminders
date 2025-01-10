package com.example.reminder.controller;

import com.example.reminder.dto.ReminderDTO;
import com.example.reminder.mapper.ReminderMapper;
import com.example.reminder.service.ReminderService;
import com.example.reminder.сontroller.ReminderController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReminderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReminderMapper reminderMapper;

    @MockBean
    private ReminderService reminderService;

    private ReminderDTO reminderDTO;

    @BeforeEach
    public void setup() {

        reminderDTO = new ReminderDTO();
        reminderDTO.setId(1);
        reminderDTO.setTitle("Reminder Title");
        reminderDTO.setRemind(new Date(2024 - 12 - 28 - 22 - 10 - 55));
        reminderDTO.setDescription("This is a reminder");
        reminderDTO.setUserId(1);
    }

    @Test
    public void givenReminderDTOList_whenGetAllReminderDTOs_thenReturnReminderDTOsList() throws Exception {

        ReminderDTO reminderDTO1 = new ReminderDTO();
        reminderDTO1.setId(2);
        reminderDTO1.setTitle("Reminder Title2");
        reminderDTO1.setRemind(new Date(2025 - 01 - 01 - 01 - 10 - 55));
        reminderDTO1.setDescription("This is a reminder2");
        reminderDTO1.setUserId(2);

        List<ReminderDTO> reminderDTOListExpected = new ArrayList<>();
        reminderDTOListExpected.add(reminderDTO);
        reminderDTOListExpected.add(reminderDTO1);

        given(reminderMapper.remindersToReminderDtos(reminderService.getReminders())).willReturn(reminderDTOListExpected);

        mockMvc.perform(get("/api/v1/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Reminder Title"))
                .andExpect(jsonPath("$[0].remind").value("1970-01-01T00:00:01.897+00:00"))
                .andExpect(jsonPath("$[0].description").value("This is a reminder"))
                .andExpect(jsonPath("$[0].user_id").value(1))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void givenReminderPagination_whenGetReminderPagination_thenReturnReminderPagination() throws Exception {

        int total = 2;
        int current = 2;

        Page<ReminderDTO> reminderDTOPage = reminderMapper.remindersToPageDtos
                (reminderService.getReminderPagination(total, current, null));

        given(reminderMapper.remindersToPageDtos
                (reminderService.getReminderPagination(total, current, null))).willReturn(reminderDTOPage);

        mockMvc.perform(get("/api/v1/list?total=2&current=2"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void givenReminderPaginationSort_whenGetReminderPaginationSort_thenReturnReminderPaginationSort() throws Exception {

        int total = 2;
        int current = 2;
        String time = "2025";

        Page<ReminderDTO> reminderDTOPageSort = reminderMapper.remindersToPageDtos
                (reminderService.getReminderPagination(total, current, time));

        given(reminderMapper.remindersToPageDtos
                (reminderService.getReminderPagination(total, current, time))).willReturn(reminderDTOPageSort);

        mockMvc.perform(get("/api/v1/list?total=2&current=2&time=2025"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void givenCreateReminder_whenCreateReminder_thenReturnReminder() throws Exception {

        given(reminderService.createReminder(reminderMapper.reminderDtoToReminder(reminderDTO)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        mockMvc.perform(post("/api/v1/reminder/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reminderDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Reminder Title"))
                .andExpect(jsonPath("$.remind").value("1970-01-01T00:00:01.897+00:00"))
                .andExpect(jsonPath("$.description").value("This is a reminder"))
                .andExpect(jsonPath("$.user_id").value(1))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void givenUpdateReminder_whenUpdateReminder_thenReturnReminder() throws Exception {

        ReminderDTO reminderDTO3 = new ReminderDTO();
        reminderDTO3.setId(3);
        reminderDTO3.setTitle("Reminder Title3");
        reminderDTO3.setRemind(new Date(2025 - 01 - 03 - 03 - 03 - 55));
        reminderDTO3.setDescription("This is a reminder3");
        reminderDTO3.setUserId(3);

        given(reminderService.updateReminder(reminderDTO.getId(), reminderMapper.reminderDtoToReminder(reminderDTO)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/{id}", reminderDTO3.getId())
                        .content(objectMapper.writeValueAsString(reminderDTO3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Reminder Title3"))
                .andExpect(jsonPath("$.remind").value("1970-01-01T00:00:01.960+00:00"))
                .andExpect(jsonPath("$.description").value("This is a reminder3"))
                .andExpect(jsonPath("$.user_id").value(3))
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    public void givenDeleteReminder_whenDeleteReminder_thenReturnReminder() throws Exception {


        willDoNothing().given(reminderService).deleteReminder(reminderDTO.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/reminder/delete/{id}", reminderDTO.getId()))

                .andExpect(status().isOk())
                .andDo(print());
    }
}
