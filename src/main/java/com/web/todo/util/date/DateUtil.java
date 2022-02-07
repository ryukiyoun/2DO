package com.web.todo.util.date;

import java.time.LocalDate;

public interface DateUtil {
    LocalDate stringToLocalDate(String dateString, String format);
    String localDateToString(LocalDate date, String format);
}