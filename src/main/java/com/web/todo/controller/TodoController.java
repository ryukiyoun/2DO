package com.web.todo.controller;

import com.web.todo.dto.UserDTO;
import com.web.todo.entity.Todo;
import com.web.todo.service.TodoFindService;
import com.web.todo.service.TodoSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TodoController {
    private final TodoFindService todoFindService;
    private final TodoSaveService todoSaveService;

    @GetMapping("/2do")
    public ModelAndView accessPage(ModelAndView mav){
        mav.setViewName("page/todo");
        return mav;
    }

    @GetMapping("/2do/{dateString}")
    public ResponseEntity<List<Todo>> findTodos(@PathVariable String dateString,
                                                @AuthenticationPrincipal UserDTO user){

        return ResponseEntity.ok().body(todoFindService.findTodoByUserIdAndDate(user.getId(), dateString));
    }

    @PostMapping("/2do")
    public ResponseEntity<Long> saveTodo(@RequestBody Todo todo,
                                         @AuthenticationPrincipal UserDTO user){

        return ResponseEntity.ok().body(todoSaveService.saveTodo(todo, user));
    }
}
