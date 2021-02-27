package com.github.rixwwd.vaccination_scheduler.pub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@SpringBootApplication
@EnableJdbcAuditing
public class VaccinationSchedulerPublicApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinationSchedulerPublicApplication.class, args);
	}

}
