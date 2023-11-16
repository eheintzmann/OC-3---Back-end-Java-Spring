package com.openclassrooms.api;

import com.openclassrooms.api.service.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OC3Application class
 */
@SpringBootApplication
public class OC3Application implements CommandLineRunner {
	private final StorageService storageService;

	/**
	 * Constructor for OC3Application class
	 *
	 * @param storageService StorageService
	 */
	public OC3Application(StorageService storageService) {
		this.storageService = storageService;
	}

	/**
	 * main method of application
	 *
	 * @param args String[]
	 */
	public static void main(String[] args) {
		SpringApplication.run(OC3Application.class, args);
	}

	/**
	 * Init StorageService first
	 *
	 * @param args String...
	 */
	@Override
	public void run(String... args) {
		storageService.init();
	}
}
