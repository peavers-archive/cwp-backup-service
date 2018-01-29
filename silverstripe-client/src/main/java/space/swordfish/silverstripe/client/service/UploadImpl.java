package space.swordfish.silverstripe.client.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.client.domain.Stack;

import java.io.IOException;

@Slf4j
@Service
public class UploadImpl implements Upload {

    private final WebClient webClient;

    public UploadImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void process() {
        webClient
                .get()
                .uri("/projects")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Stack.class))
                .log()

                // for each stack
                .flatMap(stack -> webClient
                        .get()
                        .uri("/project/{project_id}/snapshots", stack.getName())
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .flatMap(response -> response.toEntity(String.class))
                        .flatMapMany(this::transformPayloadToSnapshot)

                        // for each snapshot
                        .flatMap(downloadLink -> webClient
                                .post()
                                .uri("/amazon/{projectId}/upload", stack.getName())
                                .accept(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromObject(downloadLink))
                                .exchange()
                        )).flatMap(downloadLink -> downloadLink.bodyToFlux(String.class))

                .subscribe(System.out::println);
    }

    private Mono<String> transformPayloadToSnapshot(ResponseEntity<String> payload) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode node = objectMapper.readTree(payload.getBody());
            JsonNode links = node.findValue("links");
            JsonNode linksLinks = links.findValue("links");
            JsonNode downloadLink = linksLinks.findValue("download_link");
            JsonNode href = downloadLink.findValue("href");

            return Mono.just(href.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

