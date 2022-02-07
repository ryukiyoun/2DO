package com.web.todo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name="i_todo_id", columnList = "todo_id"))
public class AttachFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Todo todo;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String managerName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String extension;
}
