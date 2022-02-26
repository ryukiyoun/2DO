package com.web.todo.service;

import com.web.todo.dto.TodoDTO;
import com.web.todo.dto.UserDTO;
import com.web.todo.entity.Todo;
import com.web.todo.enumable.TodoState;
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

    @Transactional
    public int changeProgress(Todo todo){
        Todo searchTodo = todoRepository.findById(todo.getId()).orElse(null);

        if(searchTodo != null) {
            searchTodo.progressUpdate(todo.getProgress());
            return searchTodo.getProgress();
        }

        return 0;
    }

    @Transactional
    public TodoState changeState(Todo todo){
        Todo searchTodo = todoRepository.findById(todo.getId()).orElse(null);

        if(searchTodo != null) {
            searchTodo.stateUpdate(todo.getState());
            return searchTodo.getState();
        }

        return null;
    }
}
