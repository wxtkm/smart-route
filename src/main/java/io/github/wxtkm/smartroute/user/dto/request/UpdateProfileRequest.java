package io.github.wxtkm.smartroute.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[\\p{L}]+(?:[ '-][\\p{L}]+)*$")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[\\p{L}]+(?:[ '-][\\p{L}]+)*$")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String username;
}
