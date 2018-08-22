package tools.webkit.site.base64;

import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tools.webkit.site.base64.dto.Base64Response;

import java.nio.charset.UnsupportedCharsetException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class Base64ServiceTest {

    @InjectMocks
    private Base64Service base64Service;

    @Mock
    private Base64Repository repository;

    @Test(expected = IllegalArgumentException.class)
    public void encode_throwsIllegalArgumentException_whenInputIsNull() {
        base64Service.encode(null);
    }

    @Test
    public void encode_returnExpectedResponse_whenValidInput() {
        // Arrange
        var input = "Hello, World!";
        var output = "SGVsbG8sIFdvcmxkIQ==";

        // Act
        var response = base64Service.encode(input);

        // Assert
        assertNotNull(response.getDate());
        assertEquals(input, response.getInput());
        assertEquals(output, response.getOutput());
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_throwsIllegalArgumentException_whenInputIsNull() {
        var charset = "UTF-8";
        base64Service.decode(null, charset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_throwsIllegalArgumentException_whenCharsetIsNull() {
        var input = "Hello, World!";
        base64Service.decode(input, null);
    }

    @Test
    public void decode_returnExpectedResponse_whenValidInput() {
        // Arrange
        var input = "SGVsbG8sIFdvcmxkIQ==";
        var output = "Hello, World!";
        var charset = "UTF-8";

        // Act
        var response = base64Service.decode(input, charset);

        // Assert
        assertNotNull(response.getDate());
        assertEquals(input, response.getInput());
        assertEquals(output, response.getOutput());
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_throwsIllegalArgumentException_whenInvalidInput() {
        // Arrange
        var input = "SGVsb!!!";
        var charset = "UTF-8";
        base64Service.decode(input, charset);
        fail("An exception was expected");
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void decode_throwsUnsupportedCharsetException_whenWrongCharset() {
        String input = "SGVsbG8sIFdvcmxkIQ==";
        String charset = "FOO";
        base64Service.decode(input, charset);
        fail("An exception was expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByToken_throwsIllegalArgumentException_whenIdIsNull() {
        base64Service.findByToken(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByToken_throwsIllegalArgumentException_whenTokenHasInvalidFormat() {
        var token = UUID.fromString("xxxxx-xxxxx-xxxx-xxxx");
        base64Service.findByToken(token);
        fail("An exception was expected");
    }

    @Test(expected = NoSuchElementException.class)
    public void findByToken_throwsNoSuchElementException_whenNotFound() {
        // Arrange
        var token = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        when(repository.findById(token)).thenThrow(NoSuchElementException.class);

        // Act
        base64Service.findByToken(token);
        fail("An exception was expected");
    }

    @Test
    public void findByToken_returnExpectedResult_whenTokenExists() {
        // Arrange
        var token = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        var date = LocalDateTime.now();
        var input = "SGVsbG8sIFdvcmxkIQ==";
        var output = "Hello, World!";
        var expectedResponse = new Base64Response();
        expectedResponse.setToken(token);
        expectedResponse.setDate(date);
        expectedResponse.setInput(input);
        expectedResponse.setOutput(output);

        when(repository.findById(token)).thenReturn(Optional.of(expectedResponse));

        // Act
        var actualResponse = base64Service.findByToken(token);

        // Assert
        assertEquals(token, actualResponse.getToken());
        assertEquals(date, actualResponse.getDate());
        assertEquals(input, actualResponse.getInput());
        assertEquals(output, actualResponse.getOutput());
    }

}
