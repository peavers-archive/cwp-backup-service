package space.swordfish.silverstripe.service.silverstripe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.jasminb.jsonapi.annotations.Type;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Type("stacks")
public class Stack extends BaseResource {
    String name;
    String title;
    String created;
}