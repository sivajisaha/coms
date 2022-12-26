package coms.bpm.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ComsBpmSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComsBpmSchedulerApplication.class, args);
	}

}
