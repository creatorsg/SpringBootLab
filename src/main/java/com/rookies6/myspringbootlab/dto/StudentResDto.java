package com.rookies6.myspringbootlab.dto;

import com.rookies6.myspringbootlab.entity.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResDto {

    private Long id;
    private String name;
    private String studentId;
    private String email;
    private Integer grade;

    public static StudentResDto fromEntity(Student student) {
        return StudentResDto.builder()
                .id(student.getId())
                .name(student.getName())
                .studentId(student.getStudentId())
                .email(student.getEmail())
                .grade(student.getGrade())
                .build();
    }
}