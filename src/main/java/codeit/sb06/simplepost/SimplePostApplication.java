package codeit.sb06.simplepost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SimplePostApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplePostApplication.class, args);
    }

}
