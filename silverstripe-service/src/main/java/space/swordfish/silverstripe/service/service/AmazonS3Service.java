package space.swordfish.silverstripe.service.service;

import space.swordfish.silverstripe.service.silverstripe.domain.Snapshot;

public interface AmazonS3Service {

  void upload(Snapshot snapshot);

}
