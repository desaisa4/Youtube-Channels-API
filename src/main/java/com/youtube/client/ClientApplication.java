package com.youtube.client;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
	
	public static void main(String[] args) throws GeneralSecurityException, IOException {
		SpringApplication.run(ClientApplication.class, args);
		
	}

}
