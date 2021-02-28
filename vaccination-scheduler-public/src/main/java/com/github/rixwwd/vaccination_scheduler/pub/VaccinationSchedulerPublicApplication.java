package com.github.rixwwd.vaccination_scheduler.pub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VaccinationSchedulerPublicApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinationSchedulerPublicApplication.class, args);
	}

}
