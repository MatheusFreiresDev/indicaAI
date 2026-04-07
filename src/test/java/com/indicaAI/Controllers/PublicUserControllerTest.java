package com.indicaAI.Controllers;

import com.indicaAI.Controller.PublicUserController;
import com.indicaAI.dtos.UserProfileResponse;
import com.indicaAI.service.PublicUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicUserController.class)
class PublicUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublicUserService publicUserService;

    @Test
    void shouldReturnUserProfile() throws Exception {

        UserProfileResponse response =
                new UserProfileResponse("matheus", 5, 8.5);

        when(publicUserService.getUserProfile("matheus"))
                .thenReturn(response);

        mockMvc.perform(get("/users/matheus/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickName").value("matheus"))
                .andExpect(jsonPath("$.totalWatched").value(5));
    }
}