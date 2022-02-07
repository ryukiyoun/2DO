package com.web.todo.util.date;

import com.web.todo.util.date.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component(value = "simpleDateUtil")
public class SimpleDateUtil implements DateUtil {
    @Override
    public LocalDate stringToLocalDate(String dateString, String format) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
    }

    @Override
    public String localDateToString(LocalDate date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }
}
