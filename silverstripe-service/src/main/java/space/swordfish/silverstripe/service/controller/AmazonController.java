package space.swordfish.silverstripe.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.service.service.AmazonS3Service;
import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;

@Slf4j
@Controller
public class AmazonController {

  private final AmazonS3Service amazonS3Service;

  public AmazonController(AmazonS3Service amazonS3Service) {
    this.amazonS3Service = amazonS3Service;
  }

  @PostMapping("/amazon/{projectId}/upload")
  @ResponseBody
  public Mono<String> upload(@PathVariable String projectId, @RequestBody Snapshot snapshot) {

    snapshot.setProject(projectId);

    amazonS3Service.upload(snapshot);

    return Mono.just("No Deal!");
  }
}
