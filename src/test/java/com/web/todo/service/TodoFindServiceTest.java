package com.web.todo.service;

import com.web.todo.entity.Todo;
import com.web.todo.entity.User;
import com.web.todo.enumable.TodoState;
import com.web.todo.repository.TodoRepository;
import com.web.todo.util.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoFindServiceTest {
    @Mock
    TodoRepository todoRepository;

    @Mock
    DateUtil simpleDateUtil;

    @InjectMocks
    TodoFindService todoFindService;

    @Test
    public void findTodoByUserIdAndDate(){
        //given
        List<Todo> todoList = new ArrayList<>();

        todoList.add(Todo.builder()
                .user(User.builder().id(1).name("user").build())
                .contents("is first todo")
                .state(TodoState.NORMAL)
                .todoDate(LocalDate.now())
                .progress(40)
                .build());

        todoList.add(Todo.builder()
                .user(User.builder().id(1).name("user").build())
                .contents("is second todo")
                .state(TodoState.COMPLETE)
                .todoDate(LocalDate.now())
                .progress(100)
                .build());

        given(todoRepository.findAllByUser_IdAndTodoDateOrderByIdDesc(anyLong(), any(LocalDate.class))).willReturn(todoList);
        given(simpleDateUtil.stringToLocalDate(anyString(), anyString())).willReturn(LocalDate.now());

        //when
        List<Todo> result = todoFindService.findTodoByUserIdAndDate(anyLong(), anyString());

        //then
        assertThat(result.size(), is(2));
        checkTodoResult(result.get(0), todoList.get(0));
        checkTodoResult(result.get(1), todoList.get(1));
    }

    public void checkTodoResult(Todo result, Todo compared){
        assertThat(result.getUser().getId(), is(compared.getUser().getId()));
        assertThat(result.getContents(), is(compared.getContents()));
        assertThat(result.getState(), is(compared.getState()));
        assertThat(result.getTodoDate(), is(compared.getTodoDate()));
        assertThat(result.getProgress(), is(compared.getProgress()));
    }
}
