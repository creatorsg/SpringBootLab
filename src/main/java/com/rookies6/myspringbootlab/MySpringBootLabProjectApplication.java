package com.rookies6.myspringbootlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MySpringBootLabProjectApplication {
	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(MySpringBootLabProjectApplication.class);

		application.setWebApplicationType(WebApplicationType.SERVLET);
		application.run(args);
	}

}
