package space.swordfish.silverstripe.client.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${silverstripe.service}")
    private String silverstripeService;

    @Bean
    public WebClient client() {
        return WebClient.create(silverstripeService);
    }
}
