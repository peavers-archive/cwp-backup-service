package space.swordfish.silverstripe.service.configuration;

import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Getter
    @Value(value = "${silverstripe.username}")
    private String username;

    @Getter
    @Value(value = "${silverstripe.token}")
    private String token;

    @Getter
    @Value(value = "${aws.bucket}")
    private String bucket;

    public String getAuth() {
        String authString = username + ":" + token;

        return new String(Base64.encodeBase64(authString.getBytes()));
    }
}
