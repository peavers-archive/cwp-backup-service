package space.swordfish.silverstripe.service.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface SESService {

  void send(String subject, String message) throws UnsupportedEncodingException, MessagingException;

}
