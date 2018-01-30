package space.swordfish.silverstripe.client.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import space.swordfish.silverstripe.client.domain.SnapshotRequest;
import space.swordfish.silverstripe.client.domain.Stack;

@Slf4j
@Service
public class CreateImpl implements Create {

  private final WebClient webClient;

  public CreateImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public void process(String environment, String mode) {
    webClient
        .get()
        .uri("/projects")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .flatMapMany(response -> response.bodyToFlux(Stack.class))

        // for each stack
        .flatMap(
            stack ->
                webClient
                    .post()
                    .uri("/project/{project_id}/snapshots", stack.getName())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(
                        BodyInserters.fromObject(
                            new SnapshotRequest(stack.getName(), environment, mode)))
                    .exchange())
        .subscribe(System.out::println);
  }
}
