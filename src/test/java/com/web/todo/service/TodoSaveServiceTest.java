package com.web.todo.service;

import com.web.todo.dto.UserDTO;
import com.web.todo.entity.Todo;
import com.web.todo.entity.User;
import com.web.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoSaveServiceTest {
    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoSaveService todoSaveService;

    @Test
    public void saveTodo() {
        //given
        Todo todo = Todo.builder()
                .id(1)
                .user(User.builder().id(1).name("user").build())
                .contents("test contents")
                .todoDate(LocalDate.now())
                .progress(50)
                .build();

        given(todoRepository.save(any(Todo.class))).willReturn(todo);

        //when
        long id = todoSaveService.saveTodo(todo, UserDTO.builder().build());

        assertThat(id, is(todo.getId()));
    }

    @Test
    public void changeProgress() {
        //given
        Todo todo = Todo.builder()
                .id(1)
                .progress(30)
                .build();

        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));

        //when
        int progress = todoSaveService.changeProgress(todo);

        assertThat(progress, is(todo.getProgress()));
    }

    @Test
    public void changeProgressEmptyTodo() {
        //given
        Todo todo = Todo.builder()
                .id(1)
                .progress(30)
                .build();

        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        int progress = todoSaveService.changeProgress(todo);

        assertThat(progress, is(0));
    }
}
