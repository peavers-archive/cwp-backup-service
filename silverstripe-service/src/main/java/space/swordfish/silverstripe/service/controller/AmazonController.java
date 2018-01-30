package space.swordfish.silverstripe.service.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import space.swordfish.silverstripe.service.service.SlackService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
@Controller
public class AmazonController {

    private final SlackService slackService;
    @Value(value = "${silverstripe.username}")
    private String username;
    @Value(value = "${silverstripe.token}")
    private String token;
    @Value(value = "${aws.bucket}")
    private String bucket;

    public AmazonController(SlackService slackService) {
        this.slackService = slackService;
    }

    @PostMapping("/amazon/{projectId}/upload")
    @ResponseBody
    public Mono<String> upload(@PathVariable String projectId, @RequestBody String downloadLink)
            throws IOException {


        getFile(downloadLink, projectId);

//        sendToBucket(projectId + "/" + LocalDate.now() + ".sspak", getInputStream(downloadLink));

        return Mono.just(downloadLink);
    }

    private void getFile(String location, String projectId) throws IOException {
        final String link = location.replaceAll("\"", "");
        final String authString = username + ":" + token;

        Request.Get(link)
                .addHeader("Authorization", "Basic " + new String(Base64.encodeBase64(authString.getBytes())))
                .execute().saveContent(new File("/tmp/" + projectId + ".sspak"));
    }

    private void sendToBucket(String key, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, inputStream, objectMetadata);
        TransferManager transferManager = TransferManagerBuilder.defaultTransferManager();

        Upload upload = transferManager.upload(putObjectRequest);

        // block and wait for complete so we can tell the world about it.
        try {
            slackService.snapshotComplete(key, generateSignedUrl(upload.waitForUploadResult().getKey()), "db", "prod");
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private InputStream getInputStream(String location) throws IOException {
        final String link = location.replaceAll("\"", "");
        final String authString = username + ":" + token;

        URL url = new URL(link);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + new String(Base64.encodeBase64(authString.getBytes())));

        return urlConnection.getInputStream();
    }

    private String generateSignedUrl(String objectKey) throws URISyntaxException {
        AmazonS3 client = AmazonS3ClientBuilder.defaultClient();

        java.util.Date expiration = new java.util.Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 60; // 1 hour.
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, objectKey);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET); // Default.
        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toURI().toString();
    }
}
