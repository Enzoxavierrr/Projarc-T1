package com.bcopstein.ex4_lancheriaddd_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@SpringBootApplication
@ComponentScan("com.bcopstein")
public class Ex4LancheriadddV1Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex4LancheriadddV1Application.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
