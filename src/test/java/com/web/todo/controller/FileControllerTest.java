package com.web.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.todo.entity.AttachFile;
import com.web.todo.service.FileFindService;
import com.web.todo.service.FileSaveService;
import com.web.todo.service.UserFindService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FileController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@AutoConfigureWebMvc
@WithMockUser(username = "user")
class FileControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserFindService userFindService;

    @MockBean
    FileFindService fileFindService;

    @MockBean
    FileSaveService fileSaveService;

    @MockBean
    AuthenticationSuccessHandler customLoginSuccessHandler;

    @Test
    void getTodoFiles() throws Exception{
        //given
        List<AttachFile> fixture = new ArrayList<>();

        fixture.add(AttachFile.builder()
                .id(1)
                .originName("han")
                .managerName("alkdjfalieghl123124")
                .extension(".txt")
                .build());

        fixture.add(AttachFile.builder()
                .id(2)
                .originName("excel")
                .managerName("ahglkcbcxzkljd")
                .extension(".excel")
                .build());

        ObjectMapper objectMapper = new ObjectMapper();
        String fixtureJson = objectMapper.writeValueAsString(fixture);

        given(fileFindService.findFileByTodoId(anyLong())).willReturn(fixture);

        //when, then
        mockMvc.perform(get("/files/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(fixtureJson))
                .andDo(print());
    }

    @Test
    void saveTodoFiles() throws Exception{
        //given
        MockMultipartFile file1 = new MockMultipartFile("filepond", "filename-1.txt", "text/plain", "1".getBytes());

        //when then
        mockMvc.perform(multipart("/files/1")
                .file(file1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void removeFile() throws Exception{
        //given
        given(fileSaveService.removeFile(anyString())).willReturn("success");

        //when then
        mockMvc.perform(delete("/file/remove/testfile")
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}