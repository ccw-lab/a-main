package com.ccwlab.main;

import com.ccwlab.main.message.MyProcessor;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;


@OpenAPIDefinition(
		info = @Info(title = "User and Github",
				version = "0.1",
				description = "It provides operations of Repository and User. And it provides APIs for manipulating CI/CD pipelines.")
)
@SpringBootApplication
@EnableBinding(MyProcessor.class)
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}
