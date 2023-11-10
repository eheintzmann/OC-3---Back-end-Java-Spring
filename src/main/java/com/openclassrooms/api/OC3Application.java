package com.openclassrooms.api;

import com.openclassrooms.api.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OC3Application implements CommandLineRunner {
	private final StorageService storageService;

	public OC3Application(StorageService storageService) {
		this.storageService = storageService;
	}

	public static void main(String[] args) {
		SpringApplication.run(OC3Application.class, args);
	}

	@Override
	public void run(String... args) {
		storageService.init();
	}
}
