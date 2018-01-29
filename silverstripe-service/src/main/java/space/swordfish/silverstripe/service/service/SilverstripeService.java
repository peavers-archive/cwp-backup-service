package space.swordfish.silverstripe.service.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;
import space.swordfish.silverstripe.service.silverstripe.domain.SnapshotRequest;
import space.swordfish.silverstripe.service.silverstripe.domain.Stack;
import space.swordfish.silverstripe.service.silverstripe.domain.Transfer;

public interface SilverstripeService {

    Flux<Stack> listAllStacks();

    Flux<Snapshot> listAllSnapshots(String projectId);

    Mono<String> deleteSnapshot(String projectId, String snapshotId);

    Mono<Transfer> createSnapshot(SnapshotRequest snapshotRequest);
}

