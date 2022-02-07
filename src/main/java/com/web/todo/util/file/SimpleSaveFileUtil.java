package com.web.todo.util.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class SimpleSaveFileUtil implements FileUtil {
    @Value("${upload.file.dir}")
    private String saveFilePath;

    @Override
    public boolean uploadFile(Path dirPath, String saveFileName, MultipartFile file) throws IOException {
        File uploadFile = new File(dirPath.resolve(saveFileName).toUri());

        FileCopyUtils.copy(file.getBytes(), uploadFile);

        return true;
    }

    @Override
    public Path getDirPath() {
        LocalDate date = LocalDate.now();

        return Path.of(saveFilePath, date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    @Override
    public void makeDir(Path dirPath) {
        File file = new File(dirPath.toUri());
        file.mkdirs();
    }
}
