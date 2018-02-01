package space.swordfish.silverstripe.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.service.service.S3Service;
import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Controller
public class AmazonController {

  private final S3Service amazonS3Service;

  public AmazonController(S3Service amazonS3Service) {
    this.amazonS3Service = amazonS3Service;
  }

  @PostMapping("/amazon/{projectId}/upload")
  @ResponseBody
  public Mono<String> upload(@PathVariable String projectId, @RequestBody Snapshot snapshot) {

    snapshot.setProject(projectId);

    try {
      amazonS3Service.upload(snapshot);
    } catch (UnsupportedEncodingException | MessagingException e) {
      e.printStackTrace();
    }

      return Mono.just("No Deal!");
  }
}
