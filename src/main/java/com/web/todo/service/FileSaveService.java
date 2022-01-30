package com.web.todo.service;

import com.web.todo.entity.AttachFile;
import com.web.todo.entity.Todo;
import com.web.todo.repository.FileRepository;
import com.web.todo.util.file.FileUtil;
import com.web.todo.util.unique.UniqueIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileSaveService {
    private final FileUtil simpleSaveFileUtil;
    private final UniqueIdUtil<String> stringUniqueIdUtil;
    private final FileRepository fileRepository;

    @Transactional
    public void saveTodoFiles(Todo todo, MultipartFile[] files) {
        if (files.length != 0) {
            Path dirPath = simpleSaveFileUtil.getDirPath();
            Map<String, MultipartFile> saveFileMap = new HashMap<>();

            for (MultipartFile file : files) {
                String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String uniqueId = stringUniqueIdUtil.getUniqueId() + extension;
                saveFileMap.put(uniqueId, file);

                AttachFile attachFile = AttachFile.builder()
                        .originName(file.getOriginalFilename())
                        .managerName(uniqueId)
                        .extension(extension)
                        .filePath(dirPath.toString())
                        .todo(todo)
                        .build();

                fileRepository.save(attachFile);
            }

            try {
                simpleSaveFileUtil.makeDir(dirPath);

                for (String uniqueId : saveFileMap.keySet())
                    simpleSaveFileUtil.uploadFile(dirPath, uniqueId, saveFileMap.get(uniqueId));
            } catch (IOException e) {
                rollBackFile(dirPath, saveFileMap);
                throw new RuntimeException("file save error"); //todo 커스텀 Exception 변경 필요
            }
        }
    }

    public void rollBackFile(Path path, Map<String, MultipartFile> files) {
        for (String fileName : files.keySet())
            new File(path.resolve(fileName).toUri()).delete();
    }
}
