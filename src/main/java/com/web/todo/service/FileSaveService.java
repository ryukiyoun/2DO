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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileSaveService {
    private final FileUtil simpleSaveFileUtil;
    private final UniqueIdUtil<String> stringUniqueIdUtil;
    private final FileRepository fileRepository;

    @Transactional
    public String saveTodoFiles(long todoId, MultipartFile[] files) {
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
                    .todo(Todo.builder().id(todoId).build())
                    .build();

            fileRepository.save(attachFile);
        }

        try {
            simpleSaveFileUtil.makeDir(dirPath);

            for (String uniqueId : saveFileMap.keySet())
                simpleSaveFileUtil.uploadFile(dirPath, uniqueId, saveFileMap.get(uniqueId));

            return saveFileMap.keySet().iterator().next();
        } catch (IOException e) {
            rollBackFile(dirPath, saveFileMap);
            throw new RuntimeException("file save error");
        }
    }

    @Transactional
    public String removeFile(String managerName) {
        Optional<AttachFile> file = fileRepository.findAllByManagerName(managerName);

        file.ifPresent(attachFile -> new File(attachFile.getFilePath() + File.separator + managerName + attachFile.getExtension()).delete());

        return "";
    }

    public void rollBackFile(Path path, Map<String, MultipartFile> files) {
        for (String fileName : files.keySet())
            new File(path.resolve(fileName).toUri()).delete();
    }
}
