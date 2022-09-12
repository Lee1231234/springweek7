package spring.week7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Week7Application {

    public static void main(String[] args) {
        SpringApplication.run(Week7Application.class, args);
    }

}
