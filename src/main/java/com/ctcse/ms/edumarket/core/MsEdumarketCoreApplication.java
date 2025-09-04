package com.ctcse.ms.edumarket.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsEdumarketCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEdumarketCoreApplication.class, args);
	}

}
