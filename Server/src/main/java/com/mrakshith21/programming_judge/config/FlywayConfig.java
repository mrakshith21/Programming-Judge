package com.mrakshith21.programming_judge.config;

//import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class FlywayConfig {

    /**
     * This bean ensures that Flyway runs after Hibernate has created/updated the schema.
     * By making FlywayMigrationInitializer depend on the entityManagerFactory, 
     * we guarantee the execution order.
     */
//    @Bean
//    @DependsOn("entityManagerFactory")
//    public FlywayMigrationInitializer flywayMigrationInitializer(org.flywaydb.core.Flyway flyway) {
//        return new FlywayMigrationInitializer(flyway);
//    }
}
