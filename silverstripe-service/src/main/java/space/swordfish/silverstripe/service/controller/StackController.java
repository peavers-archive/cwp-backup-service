package space.swordfish.silverstripe.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import space.swordfish.silverstripe.service.service.SilverstripeService;
import space.swordfish.silverstripe.service.silverstripe.domain.Stack;

@Slf4j
@Controller
public class StackController {

  private final SilverstripeService silverstripeService;

  public StackController(
      @Qualifier("silverstripeServiceImpl") SilverstripeService silverstripeService) {
    this.silverstripeService = silverstripeService;
  }

  @GetMapping("/projects")
  @ResponseBody
  public Flux<Stack> listAllStacks() {
    return this.silverstripeService.listAllStacks();
  }
}
