package com.web.todo.service;

import com.web.todo.entity.AttachFile;
import com.web.todo.repository.FileRepository;
import com.web.todo.util.file.FileUtil;
import com.web.todo.util.unique.UniqueIdUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileSaveServiceTest {
    @TempDir
    Path tempDir;

    @Mock
    FileUtil simpleSaveFileUtil;

    @Mock
    UniqueIdUtil<String> stringUniqueIdUtil;

    @Mock
    FileRepository fileRepository;

    @Spy
    @InjectMocks
    FileSaveService fileSaveService;

    MockMultipartFile file1 = new MockMultipartFile("files", "filename-1.txt", "text/plain", "test file".getBytes());

    @Test
    void saveTodoFiles() {
        //given
        given(stringUniqueIdUtil.getUniqueId()).willReturn("testFileUnique");
        given(simpleSaveFileUtil.getDirPath()).willReturn(Path.of("./testDirectory"));

        //when
        String result = fileSaveService.saveTodoFiles(1, new MockMultipartFile[] {file1});

        //then
        verify(fileRepository, times(1)).save(any(AttachFile.class));
        assertThat(result ,is("testFileUnique.txt"));
    }

    @Test
    void saveTodoFilesThrow() throws IOException{
        //given
        given(simpleSaveFileUtil.getDirPath()).willReturn(Path.of("./testDirectory"));
        given(simpleSaveFileUtil.uploadFile(any(Path.class), anyString(), any(MultipartFile.class))).willThrow(new IOException("file save error"));

        //when, then
        assertThrows(RuntimeException.class, () -> fileSaveService.saveTodoFiles(1, new MockMultipartFile[] {file1}));
    }

    @Test
    void rollBackFile() throws IOException {
        //given
        Path file1 = Files.createFile(tempDir.resolve("test.txt"));
        Path file2 = Files.createFile(tempDir.resolve("test2.txt"));

        Set<String> keySet = new HashSet<>();
        keySet.add("test.txt");
        keySet.add("test2.txt");

        Map<String, MultipartFile> mockMap = mock(Map.class, RETURNS_DEEP_STUBS);
        given(mockMap.keySet()).willReturn(keySet);

        //when
        fileSaveService.rollBackFile(tempDir, mockMap);

        //then
        assertThat(Files.exists(file1), is(false));
        assertThat(Files.exists(file2), is(false));
    }

    @Test
    public void removeFile() {
        //given
        AttachFile mockAttachFile = mock(AttachFile.class);

        given(mockAttachFile.getExtension()).willReturn(".txt");
        given(mockAttachFile.getFilePath()).willReturn(tempDir.toString());

        given(fileRepository.findAllByManagerName(anyString()))
                .willReturn(Optional
                        .of(mockAttachFile));

        //when
        fileSaveService.removeFile("test");

        //then
        verify(mockAttachFile, times(1)).getExtension();
        verify(mockAttachFile, times(1)).getFilePath();
    }
}