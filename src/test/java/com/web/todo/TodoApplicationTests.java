package com.web.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class TodoApplicationTests {
	@Test
	void contextLoads() {
		TodoApplication.main(new String[] {});
	}
}
