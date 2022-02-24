package com.web.todo.service;

import com.web.todo.dto.UserDTO;
import com.web.todo.entity.AttachFile;
import com.web.todo.entity.Todo;
import com.web.todo.entity.User;
import com.web.todo.repository.FileRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FileFindServiceTest {
    @TempDir
    static Path tempDir;

    @Mock
    FileRepository fileRepository;

    @InjectMocks
    FileFindService fileFindService;

    static AttachFile attachFileFix1;
    static AttachFile attachFileFix2;

    static UserDTO user;

    @BeforeAll
    public static void init(){
        attachFileFix1 = AttachFile.builder()
                .id(1)
                .todo(Todo.builder().id(1).user(User.builder().id(1).name("tester").build()).build())
                .filePath(tempDir.toString())
                .originName("file1")
                .managerName("manager1.txt")
                .extension("txt")
                .build();

        attachFileFix2 = AttachFile.builder()
                .id(2)
                .todo(Todo.builder().id(2).user(User.builder().id(1).name("tester").build()).build())
                .filePath(tempDir.toString())
                .originName("file2")
                .managerName("manager2.txt")
                .extension("txt")
                .build();

        user = UserDTO.builder().id(1).name("tester").build();
    }

    @Test
    void findFileByTodoId() {
        //given
        List<AttachFile> list = new ArrayList<>();

        list.add(attachFileFix1);
        list.add(attachFileFix2);

        given(fileRepository.findAllByTodo_Id(anyLong())).willReturn(list);

        //when
        List<AttachFile> result = fileFindService.findFileByTodoId(1);

        //then
        assertThat(result.size(), is(2));
        checkTodoResult(result.get(0), list.get(0));
        checkTodoResult(result.get(1), list.get(1));
    }

    @Test
    public void findFileById(){
        //given
        given(fileRepository.findById(anyLong())).willReturn(Optional.of(attachFileFix1));

        //when
        AttachFile result = fileFindService.findFileById(1);

        //then
        assertThat(result, is(attachFileFix1));
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
        Path file = Files.createFile(tempDir.resolve("manager1.txt"));
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file));

        given(fileRepository.findById(anyLong()))
                .willReturn(Optional.of(attachFileFix1));

        //when
        Resource result = fileFindService.findFile(1, user);

        //then
        assertThat(result, is(resource));
    }

    @Test
    public void findFileEmptyFile(){
        //given
        given(fileRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when, then
        assertThrows(RuntimeException.class, () -> fileFindService.findFile(1, user));
    }

    @Test
    public void findFileIOException(){
        //given
        given(fileRepository.findById(anyLong()))
                .willReturn(Optional.of(attachFileFix2));

        //when, then
        assertThrows(RuntimeException.class, () -> fileFindService.findFile(1, user));
    }

    @Test
    public void findFileNotAvailableUserException(){
        //given
        given(fileRepository.findById(anyLong()))
                .willReturn(Optional.of(attachFileFix2));

        //when, then
        assertThrows(RuntimeException.class, () -> fileFindService.findFile(1, user));
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