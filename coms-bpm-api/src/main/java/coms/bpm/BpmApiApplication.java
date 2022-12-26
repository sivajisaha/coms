package coms.bpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@EnableAsync
public class BpmApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpmApiApplication.class, args);
	}

}
