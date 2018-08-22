package tools.webkit.site.base64;

import com.google.common.base.Preconditions;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import tools.webkit.site.base64.dto.Base64Response;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class Base64Service {

    @Autowired
    private Base64Repository repository;

    public Base64Response encode(String input) {
        Preconditions.checkArgument(Objects.nonNull(input), "'input' cannot be null");
        var response = new Base64Response();
        response.setDate(LocalDateTime.now());
        response.setInput(input);
        response.setOutput(Base64Utils.encodeToString(input.getBytes()));
        repository.save(response);
        return response;
    }

    public Base64Response decode(String input, String charset) {
        Preconditions.checkArgument(Objects.nonNull(input), "'input' cannot be null");
        Preconditions.checkArgument(Objects.nonNull(charset), "'charset' cannot be null");

        var rawOutput = Base64Utils.decode(input.getBytes());
        var output = new String(rawOutput, Charset.forName(charset));

        var response = new Base64Response();
        response.setDate(LocalDateTime.now());
        response.setInput(input);
        response.setOutput(output);

        repository.save(response);
        return response;
    }

    public Base64Response findByToken(UUID token) {
        Preconditions.checkArgument(Objects.nonNull(token), "'token' cannot be null");
        var record = repository.findById(token);
        return record.orElseThrow(() -> new NoSuchElementException("Token does not correspond to any record"));
    }

    public void deleteBefore(LocalDateTime date) {
        Preconditions.checkArgument(Objects.nonNull(date), "'date' cannot be null");
        repository.deleteByDateBefore(date);
    }

}
