package at.c02.aai.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EntityScan(basePackages = { "at.c02.aai.app.db.entity" })
public class AaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AaiApplication.class, args);
	}
}
