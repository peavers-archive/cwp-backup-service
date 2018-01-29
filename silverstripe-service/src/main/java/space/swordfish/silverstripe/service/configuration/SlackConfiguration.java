package space.swordfish.silverstripe.service.configuration;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SlackConfiguration {

    @Value(value = "${slack.token}")
    private String token;

    @Value(value = "${slack.channel}")
    private String slackChannel;

    @Bean
    public SlackSession slackSession() {
        SlackSession webSocketSlackSession = SlackSessionFactory.createWebSocketSlackSession(token);

        try {
            webSocketSlackSession.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return webSocketSlackSession;
    }
}
