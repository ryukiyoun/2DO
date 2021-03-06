package com.web.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.todo.dto.UserDTO;
import com.web.todo.entity.AttachFile;
import com.web.todo.service.FileFindService;
import com.web.todo.service.FileSaveService;
import com.web.todo.service.UserFindService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FileController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
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

    static AttachFile attachFileFixture1;
    static AttachFile attachFileFixture2;

    @BeforeAll
    public static void init(){
        attachFileFixture1 = AttachFile.builder()
                .id(1)
                .originName("han")
                .managerName("alkdjfalieghl123124")
                .extension(".txt")
                .build();

        attachFileFixture2 = AttachFile.builder()
                .id(2)
                .originName("excel")
                .managerName("ahglkcbcxzkljd")
                .extension(".excel")
                .build();
    }

    @Test
    void getTodoFiles() throws Exception{
        //given
        List<AttachFile> fixture = new ArrayList<>();

        fixture.add(attachFileFixture1);
        fixture.add(attachFileFixture2);

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
    void downloadFile() throws Exception {
        //given
        Resource resource = new ByteArrayResource("testFile Contents".getBytes());

        given(fileFindService.findFileById(anyLong())).willReturn(attachFileFixture1);
        given(fileFindService.findFile(anyLong(), any(UserDTO.class))).willReturn(resource);

        String fileName = "attachment; fileName=\"" + (URLEncoder.encode(attachFileFixture1.getOriginName(), StandardCharsets.UTF_8)+"\"").replace('+', ' ');

        //when, then
        mockMvc.perform(get("/file/download/1")
                .with(user(UserDTO.builder()
                        .id(1)
                        .name("user")
                        .password("pass").build())))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, fileName))
                .andExpect(header().string(HttpHeaders.PRAGMA, "no-cache"))
                .andExpect(header().string(HttpHeaders.EXPIRES, "0"))
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate"))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes("testFile Contents".getBytes()))
                .andDo(print());
    }

    @Test
    void saveTodoFiles() throws Exception{
        //given
        MockMultipartFile file1 = new MockMultipartFile("filepond", "filename-1.txt", "text/plain", "1".getBytes());

        given(fileSaveService.saveTodoFiles(anyLong(), any(MultipartFile[].class))).willReturn("manage file string");

        //when then
        mockMvc.perform(multipart("/files/1")
                .file(file1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("manage file string"))
                .andDo(print());
    }

    @Test
    void removeFile() throws Exception{
        //given
        doNothing().when(fileSaveService).removeFile(anyString());

        //when then
        mockMvc.perform(delete("/file/remove/testfile")
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}