package com.web.todo.util.unique;

import com.web.todo.util.unique.UniqueIdUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringUniqueIdUtil implements UniqueIdUtil<String> {
    @Override
    public String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}
