package com.web.todo.entity;

import com.web.todo.enumable.TodoState;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate todoDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Enumerated(value = EnumType.STRING)
    private TodoState state;

    @ColumnDefault(value = "0")
    private int progress;
}
