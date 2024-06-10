package com.sharp.vn.its.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories( "com.sharp.vn.its.management.repositories")
@EntityScan("com.sharp.vn.its.management.entity")
public class ItsManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsManagementApplication.class, args);
    }

}
