package com.web.todo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.web.todo.entity.Todo;
import com.web.todo.entity.User;
import com.web.todo.exception.InvalidJsonParamException;
import com.web.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoSaveServiceTest {
    @Mock
    TodoRepository todoRepository;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    FileSaveService fileSaveService;

    @InjectMocks
    TodoSaveService todoSaveService;

    @Test
    public void saveTodo() throws Exception{
        //given
        Todo todo = Todo.builder()
                .id(1)
                .user(User.builder().id(1).name("user").build())
                .contents("test contents")
                .todoDate(LocalDate.now())
                .progress(50)
                .build();

        given(objectMapper.readValue(anyString(), any(Class.class))).willReturn(todo);
        given(todoRepository.save(any(Todo.class))).willReturn(todo);

        given(objectMapper.enable(any(SerializationFeature.class))).willReturn(objectMapper);
        given(objectMapper.registerModule(any(JavaTimeModule.class))).willReturn(objectMapper);

        //when
        long id = todoSaveService.saveTodo("", new MultipartFile[] {});

        assertThat(id, is(todo.getId()));
    }

    @Test
    public void saveTodoThrowException() throws Exception{
        //given
        given(objectMapper.readValue(anyString(), any(Class.class)))
                .willThrow(new JsonProcessingException("json error") {});

        //when then
        assertThrows(InvalidJsonParamException.class, () -> todoSaveService.saveTodo("",  new MultipartFile[] {}));
    }
}
