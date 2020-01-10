package com.phyohtet.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringBootWebSocketChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebSocketChatApplication.class, args);
	}

	@Bean("session-store")
	public Map<String, String> sessionStore() {
		return new HashMap<>();
	}

}
