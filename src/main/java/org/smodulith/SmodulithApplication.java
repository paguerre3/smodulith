package org.smodulith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;


/*
@EnableAsync annotation in Spring is used to enable Spring's
asynchronous method execution capability.
When you annotate a method with @Async,
the method will be executed in a separate thread,
allowing the main thread to continue processing without waiting for the method to complete.
E.g. @Async public void sendEmail(String recipient)

@ConfigurationPropertiesScan is an annotation in Spring Boot
that automatically scans your application for classes annotated
with @ConfigurationProperties and registers them as beans
in the Spring context.
 */
@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan
public class SmodulithApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmodulithApplication.class, args);
	}
}
