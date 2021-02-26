package com.github.rixwwd.vaccination_scheduler.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@SpringBootApplication
@EnableJdbcAuditing
public class VaccinationSchedulerAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinationSchedulerAdminApplication.class, args);
	}

}
