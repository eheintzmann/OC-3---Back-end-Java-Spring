package com.openclassrooms.api;

import com.openclassrooms.api.controller.AuthentificationController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OC3ApplicationTests {

	@Autowired
	private AuthentificationController authentificationController;

	@Test
	void contextLoads() {
		Assertions.assertThat(authentificationController).isNotNull();
	}

}
