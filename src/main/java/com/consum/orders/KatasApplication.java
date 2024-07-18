package com.consum.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@EntityScan(basePackages = "com.consum.orders.infrastructure.database.entity")
@EnableJpaRepositories(basePackages = "com.consum.orders.infrastructure.database.repository")
@SpringBootApplication
public class KatasApplication {

	public static void main(String[] args) {
		SpringApplication.run(KatasApplication.class, args);
	}

}
