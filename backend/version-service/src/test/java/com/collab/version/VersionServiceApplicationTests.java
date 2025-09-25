package com.collab.version;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
@SpringBootTest(properties = "spring.profiles.active=test")
class VersionServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
