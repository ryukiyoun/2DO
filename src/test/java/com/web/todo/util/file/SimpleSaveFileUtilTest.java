package com.web.todo.util.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SimpleSaveFileUtilTest {
    @TempDir
    Path tempDir;

    @InjectMocks
    SimpleSaveFileUtil simpleSaveFileUtil;

    @BeforeEach
    public void Init(){
        ReflectionTestUtils.setField(simpleSaveFileUtil, "saveFilePath", tempDir.toString());
    }

    @Test
    void testUploadFile() throws IOException {
        //given
        MockMultipartFile file = new MockMultipartFile("files", "filename-1.txt", "text/plain", "test file".getBytes());

        //when
        boolean result = simpleSaveFileUtil.uploadFile(tempDir, "filename-1.txt", file);

        //then
        assertTrue(result);
        assertTrue(new File(tempDir.resolve("filename-1.txt").toUri()).exists());
    }

    @Test
    void getDirPath() {
        //given
        String dateFormatting = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //when
        Path dirPath = simpleSaveFileUtil.getDirPath();

        //then
        assertThat(dirPath.toString(), is(Path.of(tempDir.toString(), dateFormatting).toString()));
    }

    @Test
    void makeDir() {
        //given
        Path path = tempDir.resolve("makeDir");

        //when
        simpleSaveFileUtil.makeDir(path);

        //then
        assertTrue(new File(path.toUri()).exists());
    }
}