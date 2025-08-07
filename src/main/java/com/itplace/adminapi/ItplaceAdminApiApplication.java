package com.itplace.adminapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
        "com.itplace.adminapi.benefit",
        "com.itplace.adminapi.common",
        "com.itplace.adminapi.favorite",
        "com.itplace.adminapi.history",
        "com.itplace.adminapi.partner",
        "com.itplace.adminapi.security",
        "com.itplace.adminapi.user"
    }
)
@EnableMongoRepositories(
        basePackages = "com.itplace.adminapi.log"
)
public class ItplaceAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItplaceAdminApiApplication.class, args);
    }

}
