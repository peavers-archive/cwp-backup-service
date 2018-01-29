package space.swordfish.silverstripe.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestProvider;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import space.swordfish.silverstripe.service.service.SlackService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import static space.swordfish.silverstripe.service.service.SigningService.presignS3DownloadLink;

@Slf4j
@Controller
public class AmazonController {

    @Value(value = "${silverstripe.username}")
    private String username;

    @Value(value = "${silverstripe.token}")
    private String token;

    @Value(value = "${aws.bucket}")
    private String bucket;

    private final SlackService slackService;

    public AmazonController(SlackService slackService) {
        this.slackService = slackService;
    }

    @PostMapping("/amazon/{projectId}/upload")
    @ResponseBody
    public Mono<String> upload(@PathVariable String projectId, @RequestBody String downloadLink) throws IOException {
        final String key = projectId + "/" + LocalDate.now() + ".sspak";

        InputStream inputStream = getFileDownloadStream(downloadLink);
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        File file = new File("/tmp/" + key);
        FileUtils.copyInputStreamToFile(byteArrayResource.getInputStream(), file);

        uploadFile(byteArrayResource, file, key, projectId);

        return Mono.just(downloadLink);
    }

    private void uploadFile(ByteArrayResource resource, File file, String key, String projectId) throws IOException {
        FileUtils.copyInputStreamToFile(resource.getInputStream(), file);

        S3AsyncClient client = S3AsyncClient.create();
        CompletableFuture<PutObjectResponse> future = client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                AsyncRequestProvider.fromFile(Paths.get(file.toURI()))
        );

        future.whenComplete((response, err) -> {
            String path = presignS3DownloadLink(bucket, projectId + "/" + file.getName()).toString();

            slackService.snapshotComplete(projectId, path, "database", "production");

            file.delete();
        });
    }

    private InputStream getFileDownloadStream(String location) {
        final String link = location.replaceAll("\"", "");
        final String authString = username + ":" + token;

        try {
            URL url = new URL(link);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setRequestProperty("Authorization", "Basic " + new String(Base64.encodeBase64(authString.getBytes())));
            return urlConnection.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
