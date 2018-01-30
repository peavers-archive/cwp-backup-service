package space.swordfish.silverstripe.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.service.service.SilverstripeService;
import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;
import space.swordfish.silverstripe.service.silverstripe.domain.SnapshotRequest;
import space.swordfish.silverstripe.service.silverstripe.domain.Transfer;

@Slf4j
@Controller
@RequestMapping("/project")
public class SnapshotController {

  private final SilverstripeService silverstripeService;

  public SnapshotController(
      @Qualifier("silverstripeServiceImpl") SilverstripeService silverstripeService) {
    this.silverstripeService = silverstripeService;
  }

  @GetMapping("/{projectId}/snapshots")
  @ResponseBody
  public Flux<Snapshot> listAllSnapshots(@PathVariable String projectId) {
    log.info("listAllSnapshots {}", projectId);

    return this.silverstripeService.listAllSnapshots(projectId);
  }

  @PostMapping("/{projectId}/snapshots")
  @ResponseBody
  public Mono<Transfer> createSnapshot(@RequestBody SnapshotRequest snapshotRequest) {
    log.info("createSnapshot {}", snapshotRequest);

    return this.silverstripeService.createSnapshot(snapshotRequest);
  }

  @DeleteMapping("/{projectId}/snapshots/{snapshotId}")
  @ResponseBody
  public Mono<String> deleteSnapshot(
      @PathVariable String projectId, @PathVariable String snapshotId) {
    log.info("deleteSnapshot {}:{}", projectId, snapshotId);

    return this.silverstripeService.deleteSnapshot(projectId, snapshotId);
  }
}
