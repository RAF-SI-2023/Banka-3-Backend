package rs.edu.raf.userservice.domains.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    @JsonProperty("error_code")
    private ErrorCode errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

    private Instant timestamp;
}
