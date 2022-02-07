package com.web.todo.repository;

import com.web.todo.entity.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<AttachFile, Long> {
    List<AttachFile> findAllByTodo_Id(long id);

    Optional<AttachFile> findAllByManagerName(String managerName);
}
