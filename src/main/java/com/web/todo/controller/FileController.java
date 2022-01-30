package com.web.todo.controller;

import com.web.todo.entity.AttachFile;
import com.web.todo.service.FileFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileFindService fileFindService;

    @GetMapping("/files/{todoId}")
    public ResponseEntity<List<AttachFile>> getTodoFiles(@PathVariable long todoId){
        return ResponseEntity.ok().body(fileFindService.findFileByTodoId(todoId));
    }
}
