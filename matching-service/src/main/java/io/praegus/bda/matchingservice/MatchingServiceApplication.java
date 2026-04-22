package io.praegus.bda.matchingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MatchingServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(MatchingServiceApplication.class, args);
	}
}
