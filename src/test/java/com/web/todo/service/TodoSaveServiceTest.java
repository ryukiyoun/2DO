package com.web.todo.service;

import com.web.todo.dto.UserDTO;
import com.web.todo.entity.Todo;
import com.web.todo.entity.User;
import com.web.todo.enumable.TodoState;
import com.web.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoSaveServiceTest {
    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoSaveService todoSaveService;

    static Todo todoFixture;

    @BeforeAll
    public static void init(){
        todoFixture = Todo.builder()
                .id(1)
                .user(User.builder().id(1).name("user").build())
                .contents("test contents")
                .todoDate(LocalDate.now())
                .progress(50)
                .build();
    }

    @Test
    public void saveTodo() {
        //given
        given(todoRepository.save(any(Todo.class))).willReturn(todoFixture);

        //when
        long id = todoSaveService.saveTodo(todoFixture, UserDTO.builder().id(1).build());

        //then
        assertThat(id, is(todoFixture.getId()));
    }

    @Test
    public void changeProgress() {
        //given
        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todoFixture));

        //when
        int progress = todoSaveService.changeProgress(Todo.builder().id(1).progress(58).build());

        //then
        assertThat(progress, is(58));
    }

    @Test
    public void changeProgressEmptyTodo() {
        //given
        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        int progress = todoSaveService.changeProgress(Todo.builder().id(100).progress(52).build());

        //then
        assertThat(progress, is(0));
    }

    @Test
    public void changeState() {
        //given
        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todoFixture));

        //when
        TodoState state = todoSaveService.changeState(Todo.builder().id(1).state(TodoState.COMPLETE).build());

        //then
        assertThat(state, is(TodoState.COMPLETE));
    }

    @Test
    public void changeStateEmptyTodo() {
        //given
        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        TodoState state = todoSaveService.changeState(Todo.builder().id(100).state(TodoState.COMPLETE).build());

        //then
        assertThat(state, is(nullValue()));
    }
}
