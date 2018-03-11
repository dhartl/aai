package at.c02.aai.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = { "at.c02.aai.app.db.entity" })
public class AaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AaiApplication.class, args);
	}
}
