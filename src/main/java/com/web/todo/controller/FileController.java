package com.web.todo.controller;

import com.web.todo.entity.AttachFile;
import com.web.todo.service.FileFindService;
import com.web.todo.service.FileSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileFindService fileFindService;
    private final FileSaveService fileSaveService;

    @GetMapping("/files/{todoId}")
    public ResponseEntity<List<AttachFile>> getTodoFiles(@PathVariable long todoId){
        return ResponseEntity.ok().body(fileFindService.findFileByTodoId(todoId));
    }

    @GetMapping("/file/download/{attachId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable long attachId){
        HttpHeaders header = new HttpHeaders();

        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; fileName=\"" + (URLEncoder.encode(fileFindService.findFileById(attachId).getOriginName(), StandardCharsets.UTF_8)+"\"").replace('+', ' '));
        header.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        header.add(HttpHeaders.PRAGMA, "no-cache");
        header.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileFindService.findFile(attachId));
    }

    @PostMapping("/files/{todoId}")
    public ResponseEntity<String> saveTodoFiles(@PathVariable long todoId, @RequestPart("filepond") MultipartFile[] files){
        return ResponseEntity.ok().body(fileSaveService.saveTodoFiles(todoId, files));
    }

    @DeleteMapping("/file/remove/{managerName}")
    public ResponseEntity<String> removeTodoFile(@PathVariable String managerName){
        return ResponseEntity.ok().body(fileSaveService.removeFile(managerName));
    }
}
