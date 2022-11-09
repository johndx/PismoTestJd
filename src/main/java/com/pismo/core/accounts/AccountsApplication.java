package com.pismo.core.accounts;

import com.pismo.core.accounts.utils.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot Application class, responsible for boot loading the web app
 * and defining any configuration ( @Configuration and @Bean).
 */
@SpringBootApplication
public class AccountsApplication {

	private final Logger log = LoggerFactory.getLogger(AccountsApplication.class);

	@Autowired
	private DataLoader dataLoader;

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}


	/**
	 * Command line runner to execute initialisation configuration at
	 * App startup, could also be accomplished via "@PostConstryct"
	 * annotated method.
	 * @param ctx
	 * @return
	 */
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			log.info("Cmd Line Runner - performing app initialisation");
			dataLoader.loadBaseData();
			dataLoader.loadSampleData();
		};
	}

}
