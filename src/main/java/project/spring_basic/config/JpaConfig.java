package project.spring_basic.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "project.spring_basic.repository")
@EntityScan(basePackages = "project.spring_basic.entity")
public class JpaConfig {
}
