package space.swordfish.silverstripe.service.service;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SlackServiceImpl implements SlackService {

  private final SlackSession slackSession;
  @Value(value = "${slack.channel}")
  private String slackChannel;

  public SlackServiceImpl(SlackSession slackSession) {
    this.slackSession = slackSession;
  }

  @Override
  public void message(String message, SlackAttachment slackAttachment) {

    SlackChannel channel = slackSession.findChannelByName(slackChannel);

    if (slackAttachment != null) {
      slackSession.sendMessage(channel, null, slackAttachment);
    } else {
      slackSession.sendMessage(channel, message);
    }
  }

  @Override
  public void snapshotComplete(String projectId, String link, String mode, String environment) {
    SlackAttachment slackAttachment = new SlackAttachment();
    slackAttachment.setColor("#36a64f");

    slackAttachment.setAuthorName(projectId);
    slackAttachment.setTitle(environment + " " + mode);
    slackAttachment.setTitleLink(link);

    message(projectId, slackAttachment);
  }
}
