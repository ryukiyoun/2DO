package com.web.todo.service;

import com.web.todo.dto.UserDTO;
import com.web.todo.entity.AttachFile;
import com.web.todo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    public AttachFile findFileById(long attachId){
        AttachFile attachFile = fileRepository.findById(attachId).orElse(null);
        if(attachFile != null)
            return attachFile;
        else
            throw new RuntimeException("not found AttachFile");
    }

    public Resource findFile(long attachId, UserDTO user){
        try {
            AttachFile attachFile = fileRepository.findById(attachId).orElse(null);

            if(attachFile != null) {
                if(attachFile.getTodo().getUser().getId() != user.getId())
                    throw new RuntimeException("Not Available File");

                File file = new File(attachFile.getFilePath() + File.separator + attachFile.getManagerName());
                Path path = Paths.get(file.getAbsolutePath());

                return new ByteArrayResource(Files.readAllBytes(path));
            }
            else
                throw new RuntimeException("not found file");
        }
        catch (IOException ioException){
            throw new RuntimeException("file download error", ioException);
        }
    }
}
