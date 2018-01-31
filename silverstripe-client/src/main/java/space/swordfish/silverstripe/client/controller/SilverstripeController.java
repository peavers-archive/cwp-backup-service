package space.swordfish.silverstripe.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.client.service.Create;
import space.swordfish.silverstripe.client.service.Delete;
import space.swordfish.silverstripe.client.service.Upload;

@Slf4j
@Controller
public class SilverstripeController {
  private final Create create;
  private final Delete delete;
  private final Upload upload;

  public SilverstripeController(Create create, Delete delete, Upload upload) {
    this.create = create;
    this.delete = delete;
    this.upload = upload;
  }

  @GetMapping("/create")
  @ResponseBody
  public Mono<String> create() {
    create.process("prod", "assets");
    create.process("prod", "db");

    return Mono.just("creating...");
  }

  @GetMapping("/delete")
  @ResponseBody
  public Mono<String> delete() {
    delete.process();

    return Mono.just("deleting...");
  }

  @GetMapping("/upload")
  @ResponseBody
  public Mono<String> upload() {
    upload.process();

    return Mono.just("uploading...");
  }
}
