package com.web.todo.util.unique;

import com.web.todo.util.unique.StringUniqueIdUtil;
import com.web.todo.util.unique.UniqueIdUtil;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class StringUniqueIdUtilTest {

    @Test
    void getUniqueId() {
        UniqueIdUtil<String> stringUniqueIdUtil = new StringUniqueIdUtil();

        assertThat(stringUniqueIdUtil.getUniqueId(), is(not(nullValue())));
    }
}