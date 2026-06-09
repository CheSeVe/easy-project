package ru.cheseve.easyproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class EasyProjectApplication {

	static void main(String[] args) {
		SpringApplication.run(EasyProjectApplication.class, args);
	}

}
