package space.swordfish.silverstripe.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SilverstripeClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SilverstripeClientApplication.class, args);
    }

}