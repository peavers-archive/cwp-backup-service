package space.swordfish.silverstripe.service.service;

import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.swordfish.silverstripe.service.configuration.EmailConfiguration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

@Slf4j
@Service
public class SESServiceImpl implements SESService {

    private final EmailConfiguration emailConfiguration;

    public SESServiceImpl(EmailConfiguration emailConfiguration) {
        this.emailConfiguration = emailConfiguration;
    }

    @Override
    public void send(String subject, String content) {
        try {
            MimeMessage message = message(subject, content);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);

            RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);

            emailConfiguration.simpleEmailService().sendRawEmail(rawEmailRequest);

        } catch (Exception e) {
            log.error("email issues {}", e.getLocalizedMessage());
        }
    }

    private MimeMessage message(String subject, String content) throws MessagingException, UnsupportedEncodingException {
        String DefaultCharSet = MimeUtility.getDefaultJavaCharset();

        MimeMessage message = emailConfiguration.messageObject();
        message.setSubject(subject, "UTF-8");

        MimeMultipart messageBody = new MimeMultipart("alternative");

        MimeBodyPart wrap = new MimeBodyPart();
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(
                MimeUtility.encodeText(content, DefaultCharSet, "B"), "text/plain; charset=UTF-8");
        textPart.setHeader("Content-Transfer-Encoding", "base64");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(
                MimeUtility.encodeText(content, DefaultCharSet, "B"), "text/html; charset=UTF-8");
        htmlPart.setHeader("Content-Transfer-Encoding", "base64");

        messageBody.addBodyPart(textPart);
        messageBody.addBodyPart(htmlPart);

        wrap.setContent(messageBody);
        MimeMultipart msg = new MimeMultipart("mixed");
        message.setContent(msg);
        msg.addBodyPart(wrap);

        return message;
    }
}