package com.web.todo.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileUtil {
    boolean uploadFile(Path dirPath, String saveFileName, MultipartFile file) throws IOException;
    Path getDirPath();
    void makeDir(Path dirPath);
}
