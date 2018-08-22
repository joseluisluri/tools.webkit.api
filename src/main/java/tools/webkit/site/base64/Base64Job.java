package tools.webkit.site.base64;

import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;

@Component
public class Base64Job {

    private static Logger LOGGER = LoggerFactory.getLogger(Base64Job.class);

    @Autowired
    private Base64Service service;

    @Value(value = "${base64.expires.lifespan: 1}")
    private int lifespan;

    @Scheduled(cron = "${base64.expires.cron-expression:0 0 * * * *}")
    public void deleteExpiredResponses() {
        LOGGER.info("Delete old responses");
        var expiryDate = LocalDateTime.now().minus(lifespan, HOURS);
        service.deleteBefore(expiryDate);
    }

}
