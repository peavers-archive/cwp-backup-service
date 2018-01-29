package space.swordfish.silverstripe.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.*;

@Configuration
public class WebClientConfiguration {

    @Value(value = "${silverstripe.username}")
    private String username;

    @Value(value = "${silverstripe.token}")
    private String token;

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl("https://dash.cwp.govt.nz")
                .filter(credentials())
                .filter(userAgent())
                .exchangeStrategies(strategies())
                .build();
    }

    private ExchangeStrategies strategies() {
        return ExchangeStrategies
                .builder()
                .codecs(clientDefaultCodecsConfigurer -> {
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(new ObjectMapper(), MediaType.valueOf("application/vnd.api+json")));
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(new ObjectMapper(), MediaType.valueOf("application/vnd.api+json")));
                }).build();
    }

    private ExchangeFilterFunction credentials() {
        return ExchangeFilterFunctions.basicAuthentication(username, token);
    }

    private ExchangeFilterFunction userAgent() {
        return (clientRequest, exchangeFunction) -> {
            ClientRequest newRequest = ClientRequest
                    .from(clientRequest)
                    .header("User-Agent", "silverstripe-service")
                    .header("X-Api-Version", "1.0")
                    .build();
            return exchangeFunction.exchange(newRequest);
        };
    }

}
