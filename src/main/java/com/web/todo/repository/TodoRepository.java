package com.web.todo.repository;

import com.web.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUser_IdAndTodoDateOrderByIdDesc(long id, LocalDate date);
}
