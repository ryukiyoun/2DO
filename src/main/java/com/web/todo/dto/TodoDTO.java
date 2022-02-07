package com.web.todo.dto;

import com.web.todo.enumable.TodoState;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoDTO {
    private long id;

    private long userId;

    private LocalDate todoDate;

    private String Title;

    private String contents;

    private TodoState state;

    private int progress;
}
