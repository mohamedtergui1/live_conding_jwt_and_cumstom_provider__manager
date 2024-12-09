package ma.tr.livr_coding.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Size(min = 6, max = 50)
        String name
        ,
        @Email
        @NotBlank
        String email
        ,
        @NotBlank
        @Size(min = 6, max = 255)
        String password
) {
}
