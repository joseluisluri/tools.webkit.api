package tools.webkit.site.base64.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Base64Request {

    @NotNull
    @Size(max = 1024000)
    private String input;

}
