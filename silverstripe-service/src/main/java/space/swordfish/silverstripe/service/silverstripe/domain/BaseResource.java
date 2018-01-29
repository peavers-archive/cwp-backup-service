package space.swordfish.silverstripe.service.silverstripe.domain;

import com.github.jasminb.jsonapi.annotations.Id;
import com.github.jasminb.jsonapi.annotations.Links;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class BaseResource {
    @Id
    String id;

    @Links
    com.github.jasminb.jsonapi.Links links;

}
