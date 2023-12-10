package hu.cubix.balage.logisticsapplication;

import hu.cubix.balage.logisticsapplication.service.InitDbService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LogisticsApplication implements CommandLineRunner {

	private final InitDbService initDbService;

	public LogisticsApplication(InitDbService initDbService) {
		this.initDbService = initDbService;
	}

	public static void main(String[] args) {
		SpringApplication.run(LogisticsApplication.class, args);
	}


	@Override
	public void run(String... args) {
		// for testing
//		initDbService.initDb();
	}
}