package eu.pibu.vtasker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties(VtaskerProperties.class)
public class VtaskerApplication {
    public static void main(String[] args) {
        SpringApplication.run(VtaskerApplication.class, args);
    }

    @Bean
    public Clock clock() {
        log.info("Initializing Clock bean");
        return new SystemClock();
    }
}