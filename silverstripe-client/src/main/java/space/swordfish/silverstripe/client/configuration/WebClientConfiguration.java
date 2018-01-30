package space.swordfish.silverstripe.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

  @Bean
  public WebClient client() {
    return WebClient.create("http://localhost:8080");
  }
}
