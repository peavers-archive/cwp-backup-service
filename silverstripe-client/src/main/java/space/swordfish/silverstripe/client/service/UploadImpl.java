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
import space.swordfish.silverstripe.client.domain.Snapshot;
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

        // for each stack
        .flatMap(
            stack ->
                webClient
                    .get()
                    .uri("/project/{project_id}/snapshots", stack.getName())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .flatMapMany(response -> response.toEntity(String.class))
                    .flatMap(this::transformPayloadToSnapshot)

                    // for each snapshot
                    .flatMap(
                        snapshot ->
                            webClient
                                .post()
                                .uri("/amazon/{projectId}/upload", stack.getName())
                                .accept(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromObject(snapshot))
                                .exchange()))
        .subscribe();
  }

  private Mono<Snapshot> transformPayloadToSnapshot(ResponseEntity<String> payload) {
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      JsonNode node = objectMapper.readTree(payload.getBody());
      JsonNode links = node.findValue("links");
      JsonNode linksLinks = links.findValue("links");
      JsonNode downloadLink = linksLinks.findValue("download_link");
      String href = downloadLink.findValue("href").textValue();

      String mode = node.findValue("mode").textValue();
      String size = node.findValue("size").textValue();

      return Mono.just(Snapshot.builder().size(size).mode(mode).href(href).build());

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
