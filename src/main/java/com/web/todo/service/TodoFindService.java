package com.web.todo.service;

import com.web.todo.entity.Todo;
import com.web.todo.repository.TodoRepository;
import com.web.todo.util.date.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoFindService {
    private final DateUtil simpleDateUtil;
    private final TodoRepository todoRepository;

    public List<Todo> findTodoByUserIdAndDate(long userId, String dateString) {
        return todoRepository.findAllByUser_IdAndTodoDateOrderByIdDesc(userId,
                simpleDateUtil.stringToLocalDate(dateString, "yyyyMMdd"));
    }
}
