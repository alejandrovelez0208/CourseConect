package com.courseConnect.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class CourseConnectAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseConnectAdminApplication.class, args);
	}

    @Bean
    PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
