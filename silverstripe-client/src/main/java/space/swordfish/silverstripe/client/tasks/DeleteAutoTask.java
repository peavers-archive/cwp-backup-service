package space.swordfish.silverstripe.client.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import space.swordfish.silverstripe.client.service.Delete;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class DeleteAutoTask {

  private final Delete delete;

  private final ThreadPoolTaskScheduler taskScheduler;

  public DeleteAutoTask(Delete delete, ThreadPoolTaskScheduler taskScheduler) {
    this.delete = delete;
    this.taskScheduler = taskScheduler;
  }

  @PostConstruct
  public void scheduleRunnableWithCronTrigger() {
    CronTrigger cronTrigger = new CronTrigger("0 0 19 1/1 * ?");
    taskScheduler.schedule(new RunnableTask(), cronTrigger);
  }

  class RunnableTask implements Runnable {

    @Override
    public void run() {
      delete.process(100);
    }
  }
}
