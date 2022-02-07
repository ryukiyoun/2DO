package com.web.todo.util.date;

import com.web.todo.util.date.DateUtil;
import com.web.todo.util.date.SimpleDateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SimpleDateUtilTest {
    DateUtil simpleDateUtil = new SimpleDateUtil();

    @Test
    void stringToLocalDate() {
        String date = "20220101";
        String format = "yyyyMMdd";

        LocalDate localDate = simpleDateUtil.stringToLocalDate(date, format);

        assertThat(date, is(localDate.format(DateTimeFormatter.ofPattern(format))));
    }

    @Test
    void localDateToString() {
        LocalDate localDate = LocalDate.of(2022, 1, 1);
        String format = "yyyyMMdd";

        String date = simpleDateUtil.localDateToString(localDate, format);

        assertThat(date, is(localDate.format(DateTimeFormatter.ofPattern(format))));
    }
}