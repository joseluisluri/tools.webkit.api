package tools.webkit.site.base64;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import tools.webkit.site.base64.dto.Base64Response;

import java.time.LocalDateTime;
import java.util.UUID;

interface Base64Repository extends CrudRepository<Base64Response, UUID> {

    @Modifying
    void deleteByDateBefore(LocalDateTime expiryDate);

}
