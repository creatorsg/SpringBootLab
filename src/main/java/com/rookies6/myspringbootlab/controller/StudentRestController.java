package com.rookies6.myspringbootlab.controller;

import com.rookies6.myspringbootlab.dto.StudentReqDto;
import com.rookies6.myspringbootlab.dto.StudentResDto;
import com.rookies6.myspringbootlab.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResDto> createStudent(@Valid @RequestBody StudentReqDto reqDto) {
        StudentResDto createdStudent = studentService.createStudent(reqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping
    public List<StudentResDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentReqDto reqDto) {
        return ResponseEntity.ok(studentService.updateStudent(id, reqDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}