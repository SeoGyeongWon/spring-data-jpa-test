package codeit.sb06.simplepost.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostDeleteRequest(
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
}