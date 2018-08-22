package tools.webkit.site.base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tools.webkit.site.base64.dto.Base64Request;
import tools.webkit.site.base64.dto.Base64Response;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/base64")
public class Base64Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base64Controller.class);

    @Value(value = "${webkit.charset}")
    private String charset;

    @Autowired
    private Base64Service base64Service;

    @GetMapping(value = "/{uuid}")
    public Base64Response share(@PathVariable(value = "uuid") UUID uuid) {
        LOGGER.info("Received get request with uuid '{}'", uuid);
        return base64Service.findByToken(uuid);
    }

    @PostMapping(value = "/encode", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Base64Response encode(@Valid @RequestBody Base64Request request) {
        LOGGER.info("Received encode request");
        return base64Service.encode(request.getInput());
    }

    @PostMapping(value = "/decode", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Base64Response decode(@Valid @RequestBody Base64Request request) {
        LOGGER.info("Received decode request");
        return base64Service.decode(request.getInput(), charset);
    }

}
