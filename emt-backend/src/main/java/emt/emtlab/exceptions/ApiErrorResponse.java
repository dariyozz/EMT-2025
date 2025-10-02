package emt.emtlab.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}