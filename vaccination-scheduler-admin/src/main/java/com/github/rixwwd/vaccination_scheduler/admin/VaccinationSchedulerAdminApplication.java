package com.github.rixwwd.vaccination_scheduler.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class VaccinationSchedulerAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinationSchedulerAdminApplication.class, args);
	}

}
