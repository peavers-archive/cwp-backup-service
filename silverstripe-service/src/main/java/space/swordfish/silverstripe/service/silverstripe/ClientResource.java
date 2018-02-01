package space.swordfish.silverstripe.service.silverstripe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jasminb.jsonapi.ResourceConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;
import space.swordfish.silverstripe.service.silverstripe.domain.Stack;
import space.swordfish.silverstripe.service.silverstripe.domain.Transfer;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ClientResource {

  Mono<Transfer> transformPayloadToTransfer(ResponseEntity<String> payload) {
    ObjectMapper objectMapper = new ObjectMapper();
    ResourceConverter resourceConverter = new ResourceConverter(objectMapper, Transfer.class);

    Transfer transfer =
        resourceConverter.readDocument(Objects.requireNonNull(payload.getBody()).getBytes(), Transfer.class).get();

    return Mono.just(transfer);
  }

  Flux<Stack> transformPayloadToStack(ResponseEntity<String> payload) {
    ObjectMapper objectMapper = new ObjectMapper();
    ResourceConverter resourceConverter = new ResourceConverter(objectMapper, Stack.class);

    List<Stack> stackList =
        resourceConverter.readDocumentCollection(Objects.requireNonNull(payload.getBody()).getBytes(), Stack.class).get();

    return Flux.fromIterable(stackList);
  }

  Flux<Snapshot> transformPayloadToSnapshot(ResponseEntity<String> payload) {
    ObjectMapper objectMapper = new ObjectMapper();
    ResourceConverter resourceConverter = new ResourceConverter(objectMapper, Snapshot.class);

    List<Snapshot> stackList =
        resourceConverter
            .readDocumentCollection(Objects.requireNonNull(payload.getBody()).getBytes(), Snapshot.class)
            .get();

    return Flux.fromIterable(stackList);
  }
}
