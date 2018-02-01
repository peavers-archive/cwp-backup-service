package space.swordfish.silverstripe.service.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Properties;

@Slf4j
@Service
public class SESServiceImpl implements SESService {

  @Value("${email.sender}")
  private String sender;

  @Value("${email.recipients}")
  private String recipients;

  @Override
  public void send(String subject, String content)
      throws UnsupportedEncodingException, MessagingException {

    String DefaultCharSet = MimeUtility.getDefaultJavaCharset();

    Session session = Session.getDefaultInstance(new Properties());

    MimeMessage message = new MimeMessage(session);
    message.setSubject(subject, "UTF-8");
    message.setFrom(new InternetAddress(sender));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
    MimeMultipart msg_body = new MimeMultipart("alternative");

    MimeBodyPart wrap = new MimeBodyPart();
    MimeBodyPart textPart = new MimeBodyPart();
    textPart.setContent(
        MimeUtility.encodeText(content, DefaultCharSet, "B"), "text/plain; charset=UTF-8");
    textPart.setHeader("Content-Transfer-Encoding", "base64");

    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(
        MimeUtility.encodeText(content, DefaultCharSet, "B"), "text/html; charset=UTF-8");
    htmlPart.setHeader("Content-Transfer-Encoding", "base64");

    msg_body.addBodyPart(textPart);
    msg_body.addBodyPart(htmlPart);
    wrap.setContent(msg_body);
    MimeMultipart msg = new MimeMultipart("mixed");
    message.setContent(msg);
    msg.addBodyPart(wrap);

    try {
      AmazonSimpleEmailService client =
          AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      message.writeTo(outputStream);
      RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
      SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
      client.sendRawEmail(rawEmailRequest);

    } catch (Exception ex) {
      log.error("email issues {}", ex.getLocalizedMessage());
      ex.printStackTrace();
    }
  }
}
