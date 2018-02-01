package space.swordfish.silverstripe.service.configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Value("${email.sender}")
    private String sender;

    @Value("${email.recipients}")
    private String recipients;

    public MimeMessage messageObject() throws MessagingException {

        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));

        return message;
    }

    public AmazonSimpleEmailService simpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    }

}
