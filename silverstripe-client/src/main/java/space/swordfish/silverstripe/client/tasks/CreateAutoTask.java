package space.swordfish.silverstripe.client.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import space.swordfish.silverstripe.client.service.Create;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class CreateAutoTask {

  private final Create create;

  private final ThreadPoolTaskScheduler taskScheduler;

  public CreateAutoTask(Create create, ThreadPoolTaskScheduler taskScheduler) {
    this.create = create;
    this.taskScheduler = taskScheduler;
  }

  @PostConstruct
  public void scheduleRunnableWithCronTrigger() {
    CronTrigger cronTrigger = new CronTrigger("0 0 20 1/1 * ?");
    taskScheduler.schedule(new RunnableTask(), cronTrigger);
  }

  class RunnableTask implements Runnable {

    @Override
    public void run() {
      create.process("prod", "assets");
      create.process("prod", "db");
    }
  }
}
