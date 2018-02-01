package space.swordfish.silverstripe.service.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.stereotype.Service;
import space.swordfish.silverstripe.service.configuration.StorageConfiguration;
import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

@Service
public class S3ServiceImpl implements S3Service {

    private final StorageConfiguration storageConfiguration;

    private final MessageService messageService;

    public S3ServiceImpl(StorageConfiguration storageConfiguration, MessageService messageService) {
        this.storageConfiguration = storageConfiguration;
        this.messageService = messageService;
    }

    @Override
    public void upload(Snapshot snapshot) throws UnsupportedEncodingException, MessagingException {
        String key = snapshot.getProject() + "/" + snapshot.getMode() + ".sspak";
        String link = snapshot.getHref();
        long contentSize = Long.valueOf(snapshot.getSize());

        InputStream inputStream = null;
        try {
            inputStream = getInputStream(link);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(contentSize);

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(storageConfiguration.getBucket(), key, inputStream, objectMetadata);

        TransferManager transferManager = TransferManagerBuilder.defaultTransferManager();
        Upload upload = transferManager.upload(putObjectRequest);

        try {
            String signUrl = signUrl(upload.waitForUploadResult().getKey());

            String bodyHtml =
                    "<html>" + "<head></head>" + "<body>" + "<a>" + signUrl + "</a>" + "</body>" + "</html>";

            messageService.email("Nightly snapshot for " + snapshot.getProject(), bodyHtml);
            messageService.slack(key, signUrl, snapshot.getMode(), "production");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String signUrl(String objectKey) {
        AmazonS3 client = AmazonS3ClientBuilder.defaultClient();

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(storageConfiguration.getBucket(), objectKey);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(signUrlExpiry());

        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);

        try {
            return url.toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Date signUrlExpiry() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        calendar.getTime();

        java.util.Date expiration = new java.util.Date();
        expiration.setTime(calendar.getTimeInMillis());

        return expiration;
    }

    private InputStream getInputStream(String location) throws IOException {
        URL url = new URL(location);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty(
                "Authorization", "Basic " + storageConfiguration.getAuth());

        return urlConnection.getInputStream();
    }
}
