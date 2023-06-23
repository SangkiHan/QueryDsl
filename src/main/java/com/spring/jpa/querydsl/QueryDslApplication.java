package com.spring.jpa.querydsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QueryDslApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryDslApplication.class, args);
	}

}
