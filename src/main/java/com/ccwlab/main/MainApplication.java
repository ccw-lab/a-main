package com.ccwlab.main;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@OpenAPIDefinition(
		info = @Info(title = "User and Github",
				version = "0.1",
				description = "It provides operations of Repository and User. And it provides APIs for manipulating CI/CD pipelines.")
)
@SpringBootApplication
//@EnableBinding(MyProcessor.class)
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@LoadBalanced
	@Bean
	public RestTemplate loadbalancedRestTemplate() {
		return new RestTemplate();
	}

//	@Bean
//	public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
//			ConfigurableApplicationContext context) {
//		return ServiceInstanceListSupplier.builder()
//				.withDiscoveryClient()
//				.withSameInstancePreference()
//				.build(context);
//	}
}
