package com.miwtech.artifactory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miwtech.artifactory.Application;
import com.miwtech.artifactory.factory.ItemFactory;
import com.miwtech.artifactory.model.Item;
import com.miwtech.artifactory.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithUserDetails("miw-user")
    void getItems() throws Exception {
        final List<Item> items = List.of(ItemFactory.getItem());
        when(itemService.getItems()).thenReturn(items);
        mockMvc.perform(get("/items")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(items)));
    }

    @Test
    @WithUserDetails("miw-user")
    void buyItem() throws Exception {
        doNothing().when(itemService).buyItem(any(Item.class));
        mockMvc.perform(post("/items")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ItemFactory.getItem())))
                .andExpect(status().isOk());
    }

}
