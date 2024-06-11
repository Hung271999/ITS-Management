package com.sharp.vn.its.management;

import com.sharp.vn.its.management.repositories.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

/**
 * The type Its management application.
 */
@SpringBootApplication
@EnableJpaRepositories(value = "com.sharp.vn.its.management.repositories",
        repositoryBaseClass = BaseJpaRepositoryImpl.class)
@EntityScan("com.sharp.vn.its.management.entity")
public class ItsManagementApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(ItsManagementApplication.class, args);
    }

}
