package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.dto.StudentReqDto;
import com.rookies6.myspringbootlab.dto.StudentResDto;
import com.rookies6.myspringbootlab.entity.Student;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResDto createStudent(StudentReqDto reqDto) {
        if (studentRepository.existsByStudentId(reqDto.getStudentId())) {
            throw new BusinessException("이미 존재하는 학번입니다: " + reqDto.getStudentId(), HttpStatus.BAD_REQUEST);
        }

        Student student = Student.builder()
                .name(reqDto.getName())
                .studentId(reqDto.getStudentId())
                .email(reqDto.getEmail())
                .grade(reqDto.getGrade())
                .build();

        Student savedStudent = studentRepository.save(student);
        return StudentResDto.fromEntity(savedStudent);
    }

    public List<StudentResDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentResDto::fromEntity)
                .collect(Collectors.toList());
    }

    public StudentResDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 학생을 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        return StudentResDto.fromEntity(student);
    }

    @Transactional
    public StudentResDto updateStudent(Long id, StudentReqDto reqDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 학생을 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        student.setName(reqDto.getName());
        student.setEmail(reqDto.getEmail());
        student.setGrade(reqDto.getGrade());

        return StudentResDto.fromEntity(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new BusinessException("해당 ID의 학생을 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND);
        }
        studentRepository.deleteById(id);
    }
}