package space.swordfish.silverstripe.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfiguration {

    @Bean
    public WebClient client() {
        return WebClient.create("http://localhost:8080");
    }

}
