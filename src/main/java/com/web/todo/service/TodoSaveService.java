package com.web.todo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.web.todo.entity.Todo;
import com.web.todo.enumable.ErrorCode;
import com.web.todo.exception.InvalidJsonParamException;
import com.web.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoSaveService {
    private final TodoRepository todoRepository;
    private final ObjectMapper objectMapper;
    private final FileSaveService fileSaveService;

    @Transactional
    public long saveTodo(String todoJson, MultipartFile[] files){
        try {
            objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.registerModule(new JavaTimeModule());

            Todo todo = objectMapper.readValue(todoJson, Todo.class);
            todoRepository.save(todo);

            fileSaveService.saveTodoFiles(todo, files);

            return todo.getId();
        }
        catch (JsonProcessingException e){
            throw new InvalidJsonParamException(todoJson, ErrorCode.INVALID_ERROR, "Invalid Json Save Parameter", e);
        }
    }
}
