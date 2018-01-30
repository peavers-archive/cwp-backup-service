package space.swordfish.silverstripe.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Type("snapshots")
public class Snapshot extends BaseResource {
  String created;
  String mode;
  String size;
  String href;

  @JsonProperty("can_download")
  String canDownload;

  @JsonProperty("can_delete")
  String canDelete;

  @JsonProperty("snapshot_status")
  String snapshotStatus;
}
