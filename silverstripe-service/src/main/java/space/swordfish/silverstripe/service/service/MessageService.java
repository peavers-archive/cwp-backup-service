package space.swordfish.silverstripe.service.service;

public interface MessageService {

    void email(String subject, String message);

    void slack(String projectId, String link, String mode, String environment);

}
