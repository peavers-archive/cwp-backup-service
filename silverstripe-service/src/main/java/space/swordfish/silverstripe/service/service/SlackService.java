package space.swordfish.silverstripe.service.service;

import com.ullink.slack.simpleslackapi.SlackAttachment;

public interface SlackService {

  void message(String message, SlackAttachment slackAttachment);

  void snapshotComplete(String projectId, String link, String mode, String environment);
}
