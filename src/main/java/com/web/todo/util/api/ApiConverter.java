package com.web.todo.util.api;

public interface ApiConverter<T> {
    T convertToObject(String apiResultStr);
}
