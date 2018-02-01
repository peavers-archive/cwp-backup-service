package space.swordfish.silverstripe.service.silverstripe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;
import space.swordfish.silverstripe.service.silverstripe.domain.SnapshotRequest;
import space.swordfish.silverstripe.service.silverstripe.domain.Stack;
import space.swordfish.silverstripe.service.silverstripe.domain.Transfer;

@Slf4j
@Component
public class SilverstripeClient extends ClientResource {

  private final WebClient webClient;

  public SilverstripeClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public Flux<Stack> listAllStacks() {
    return this.webClient
        .get()
        .uri("/naut/projects")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .flatMap(response -> response.toEntity(String.class))
        .flatMapMany(this::transformPayloadToStack);
  }

  public Flux<Snapshot> listAllSnapshots(String projectId) {
    return this.webClient
        .get()
        .uri("/naut/project/{project_id}/snapshots", projectId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .flatMap(response -> response.toEntity(String.class))
        .flatMapMany(this::transformPayloadToSnapshot);
  }

  public Mono<Transfer> createSnapshot(SnapshotRequest snapshotRequest) {
    return this.webClient
        .post()
        .uri("/naut/project/{projectId}/snapshots", snapshotRequest.getProjectId())
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(snapshotRequest))
        .exchange()
        .flatMap(response -> response.toEntity(String.class))
        .flatMap(this::transformPayloadToTransfer);
  }

  public Mono<String> deleteSnapshot(String projectId, String snapshotId) {
    return this.webClient
        .delete()
        .uri("/naut/project/{project_id}/snapshots/{snapshot_id}", projectId, snapshotId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .flatMap(response -> response.bodyToMono(String.class));
  }

  @ExceptionHandler(WebClientResponseException.class)
  public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
    log.error(
        "Error from WebClient - Status {}, Body {}",
        ex.getRawStatusCode(),
        ex.getResponseBodyAsString(),
        ex);
    return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
  }
}
