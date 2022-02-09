package com.web.todo.service;

import com.web.todo.entity.AttachFile;
import com.web.todo.entity.Todo;
import com.web.todo.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileFindServiceTest {
    @TempDir
    Path tempDir;

    @Mock
    FileRepository fileRepository;

    @InjectMocks
    FileFindService fileFindService;

    @Test
    void findFileByTodoId() {
        //given
        List<AttachFile> fixture = new ArrayList<>();

        fixture.add(AttachFile.builder()
                .id(1)
                .todo(Todo.builder().id(1).build())
                .originName("file1")
                .managerName("manager1")
                .extension("txt")
                .filePath("test/dir")
                .build());

        fixture.add(AttachFile.builder()
                .id(2)
                .todo(Todo.builder().id(1).build())
                .originName("file2")
                .managerName("manager2")
                .extension("txt")
                .filePath("test/dir")
                .build());

        given(fileRepository.findAllByTodo_Id(anyLong())).willReturn(fixture);

        //when
        List<AttachFile> result = fileFindService.findFileByTodoId(1);

        //then
        assertThat(result.size(), is(2));
        checkTodoResult(result.get(0), fixture.get(0));
        checkTodoResult(result.get(1), fixture.get(1));
    }

    @Test
    public void findFileById(){
        //given
        AttachFile file = AttachFile.builder()
                .id(1)
                .todo(Todo.builder().id(1).build())
                .originName("file1")
                .managerName("manager1")
                .extension("txt")
                .filePath("test/dir")
                .build();

        given(fileRepository.findById(anyLong())).willReturn(Optional.of(file));

        //when
        AttachFile result = fileFindService.findFileById(1);

        assertThat(result, is(file));
    }

    @Test
    public void findFileByIdEmpty(){
        //given
        given(fileRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThrows(RuntimeException.class, () -> fileFindService.findFileById(1));
    }

    @Test
    public void findFile() throws Exception{
        //given
        AttachFile attachFile = AttachFile.builder()
                .id(1)
                .filePath(tempDir.toString())
                .managerName("manager.txt")
                .originName("test.txt")
                .extension(".txt")
                .build();

        Path file = Files.createFile(tempDir.resolve("manager.txt"));
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file));

        given(fileRepository.findById(anyLong()))
                .willReturn(Optional.of(attachFile));

        Resource result = fileFindService.findFile(1);

        assertThat(result, is(resource));
    }

    @Test
    public void findFileEmptyFile() throws Exception{
        //given
        given(fileRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fileFindService.findFile(1));
    }

    @Test
    public void findFileException() throws Exception{
        //given
        AttachFile attachFile = AttachFile.builder()
                .id(1)
                .filePath(tempDir.toString())
                .managerName("manager.txt")
                .originName("test.txt")
                .extension(".txt")
                .build();

        given(fileRepository.findById(anyLong()))
                .willReturn(Optional.of(attachFile));

        assertThrows(RuntimeException.class, () -> fileFindService.findFile(1));
    }

    public void checkTodoResult(AttachFile result, AttachFile compared){
        assertThat(result.getId(), is(compared.getId()));
        assertThat(result.getOriginName(), is(compared.getOriginName()));
        assertThat(result.getManagerName(), is(compared.getManagerName()));
        assertThat(result.getExtension(), is(compared.getExtension()));
        assertThat(result.getFilePath(), is(compared.getFilePath()));
        assertThat(result.getTodo().getId(), is(compared.getTodo().getId()));
    }
}