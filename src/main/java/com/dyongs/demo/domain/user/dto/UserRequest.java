package com.dyongs.demo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequest {

    @NotBlank(message="이름은 필수 값입니다.")
    @Size(min=2, max=20, message="이름은 2~20자 사이여야 합니다.")
    private String name;

    @Email(message="올바른 이메일 형식이 아닙니다.")
    @NotBlank(message="이메일은 필수 값입니다.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "이메일 형식이 유효하지 않습니다."
    )
    private String email;
}
