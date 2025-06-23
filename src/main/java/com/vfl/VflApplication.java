package com.vfl;

import com.vfl.util.ApplicationEnvironment;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class VflApplication {

	public static void main(String[] args) {
		SpringApplication.run(VflApplication.class, args);
	}
	@Bean
	ApplicationRunner applicationRunner(Environment environment) {
		return args -> {
			System.out.println("app.work.path " + environment.getProperty("app.work.path"));
			ApplicationEnvironment.environment = environment;
		};
	}

}
