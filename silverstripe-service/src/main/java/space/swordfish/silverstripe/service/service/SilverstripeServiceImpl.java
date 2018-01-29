package space.swordfish.silverstripe.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.service.silverstripe.SilverstripeClient;
import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;
import space.swordfish.silverstripe.service.silverstripe.domain.SnapshotRequest;
import space.swordfish.silverstripe.service.silverstripe.domain.Stack;
import space.swordfish.silverstripe.service.silverstripe.domain.Transfer;

@Slf4j
@Service
public class SilverstripeServiceImpl implements SilverstripeService {

    private final SilverstripeClient silverstripeClient;

    public SilverstripeServiceImpl(SilverstripeClient silverstripeClient) {
        this.silverstripeClient = silverstripeClient;
    }

    @Override
    public Flux<Stack> listAllStacks() {
        return this.silverstripeClient.listAllStacks();
    }

    @Override
    public Flux<Snapshot> listAllSnapshots(String projectId) {
        return this.silverstripeClient.listAllSnapshots(projectId);
    }

    @Override
    public Mono<String> deleteSnapshot(String projectId, String snapshotId) {
        return this.silverstripeClient.deleteSnapshot(projectId, snapshotId);
    }

    @Override
    public Mono<Transfer> createSnapshot(SnapshotRequest snapshotRequest) {
        return this.silverstripeClient.createSnapshot(snapshotRequest);
    }

}


