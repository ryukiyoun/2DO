package com.web.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.todo.dto.UserDTO;
import com.web.todo.entity.Todo;
import com.web.todo.enumable.TodoState;
import com.web.todo.service.TodoFindService;
import com.web.todo.service.TodoSaveService;
import com.web.todo.service.UserFindService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = TodoController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@AutoConfigureWebMvc
@WithMockUser(username = "user")
class TodoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TodoFindService todoFindService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    TodoSaveService todoSaveService;

    @MockBean
    UserFindService userFindService;

    @MockBean
    AuthenticationSuccessHandler customLoginSuccessHandler;

    @Test
    public void accessPageTest() throws Exception{
        //when then
        mockMvc.perform(get("/2do"))
                .andExpect(status().isOk())
                .andExpect(view().name("page/todo"))
                .andDo(print());
    }

    @Test
    public void findTodos() throws Exception{
        //when then
        mockMvc.perform(get("/2do/20220119")
                .with(user(UserDTO.builder()
                        .id(1)
                        .name("user")
                        .password("pass").build())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void saveTodo() throws Exception{
        //given
        String content = objectMapper.writeValueAsString(Todo.builder().contents("test").state(TodoState.NORMAL).build());

        //when then
        mockMvc.perform(post("/2do")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
