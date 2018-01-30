package space.swordfish.silverstripe.client.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import space.swordfish.silverstripe.client.service.Upload;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class UploadAutoTask {

  private final Upload upload;

  private final ThreadPoolTaskScheduler taskScheduler;

  public UploadAutoTask(Upload upload, ThreadPoolTaskScheduler taskScheduler) {
    this.upload = upload;
    this.taskScheduler = taskScheduler;
  }

  @PostConstruct
  public void scheduleRunnableWithCronTrigger() {
    CronTrigger cronTrigger = new CronTrigger("0 0 23 1/1 * ?");
    taskScheduler.schedule(new RunnableTask(), cronTrigger);
  }

  class RunnableTask implements Runnable {

    @Override
    public void run() {
      upload.process();
    }
  }
}
