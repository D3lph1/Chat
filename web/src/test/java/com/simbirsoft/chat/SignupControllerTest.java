package com.simbirsoft.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.chat.dto.SignupUserDTO;
import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.service.http.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SignupControllerTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void example() throws Exception {
        SignupUserDTO dto = new SignupUserDTO(
                "John",
                "Brown",
                "example@example.com",
                "example",
                "example"
        );

        mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        ).andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .json(objectMapper.writeValueAsString(new Response(Response.STATUS_SUCCESS)))
                );
    }
}
