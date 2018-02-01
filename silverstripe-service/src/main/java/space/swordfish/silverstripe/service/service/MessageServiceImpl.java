package space.swordfish.silverstripe.service.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public class MessageServiceImpl implements MessageService {

    private final SESService sesService;

    private final SlackService slackService;

    public MessageServiceImpl(SESService sesService, SlackService slackService) {
        this.sesService = sesService;
        this.slackService = slackService;
    }

    @Override
    public void email(String subject, String message) {
        try {
            sesService.send(subject, message);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void slack(String projectId, String link, String mode, String environment) {
        slackService.snapshotComplete(projectId, link, mode, environment);
    }
}
