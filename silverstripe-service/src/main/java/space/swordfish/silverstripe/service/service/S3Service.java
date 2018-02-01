package space.swordfish.silverstripe.service.service;

import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface S3Service {

  void upload(Snapshot snapshot) throws UnsupportedEncodingException, MessagingException;

}
