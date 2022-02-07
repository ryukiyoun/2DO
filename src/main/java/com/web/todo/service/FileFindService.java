package com.web.todo.service;

import com.web.todo.entity.AttachFile;
import com.web.todo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileFindService {
    private final FileRepository fileRepository;

    public List<AttachFile> findFileByTodoId(long todoId){
        return fileRepository.findAllByTodo_Id(todoId);
    }

    public ResponseEntity<Resource> findFile(long attachId){
        try {
            AttachFile attachFile = fileRepository.findById(attachId).orElse(null);

            if(attachFile != null) {
                File file = new File(attachFile.getFilePath() + File.separator + attachFile.getManagerName());
                Path path = Paths.get(file.getAbsolutePath());
                ByteArrayResource resource =
                        new ByteArrayResource(Files.readAllBytes(path));
                HttpHeaders header = new HttpHeaders();
                header.add(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; fileName=\"" + (URLEncoder.encode(attachFile.getOriginName(), StandardCharsets.UTF_8)+"\"").replace('+', ' '));
                header.add("Cache-Control",
                        "no-cache, no-store, must-revalidate");
                header.add("Pragma", "no-cache");
                header.add("Expires", "0");
                return ResponseEntity.ok().headers(header).
                        contentLength(file.length())
                        .contentType(MediaType.
                                parseMediaType("application/octet-stream")).
                                body(resource);
            }
            else{
                throw new RuntimeException("not found file");
            }
        }
        catch (IOException ioException){
            throw new RuntimeException("file download error", ioException);
        }
    }
}
