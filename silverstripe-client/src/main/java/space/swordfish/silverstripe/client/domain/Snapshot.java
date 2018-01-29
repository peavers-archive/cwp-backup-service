package space.swordfish.silverstripe.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Links;
import com.github.jasminb.jsonapi.annotations.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Type("snapshots")
public class Snapshot extends BaseResource {
    String created;
    String mode;
    String size;

    @JsonProperty("can_download")
    String canDownload;

    @JsonProperty("can_delete")
    String canDelete;

    @JsonProperty("snapshot_status")
    String snapshotStatus;
}