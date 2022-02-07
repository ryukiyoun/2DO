package com.web.todo.service;

import com.web.todo.dto.UserDTO;
import com.web.todo.entity.Todo;
import com.web.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoSaveService {
    private final TodoRepository todoRepository;

    @Transactional
    public long saveTodo(Todo todo, UserDTO user){
        todo.setTodoUser(user.getId());
        return todoRepository.save(todo).getId();
    }
}
