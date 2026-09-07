package com.rookies6.myspringbootlab.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentReqDto {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "학번은 필수 입력 항목입니다.")
    private String studentId;

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "학년은 필수 입력 항목입니다.")
    @Min(value = 1, message = "학년은 최소 1학년 이상이어야 합니다.")
    @Max(value = 4, message = "학년은 최대 4학년 이하이어야 합니다.")
    private Integer grade;
}