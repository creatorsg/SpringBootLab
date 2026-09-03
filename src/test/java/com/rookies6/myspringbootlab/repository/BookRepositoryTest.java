package com.rookies6.myspringbootlab.repository;

import com.rookies6.myspringbootlab.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 각 테스트 종료 후 DB 롤백 처리
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        // 주어진 초기 데이터 등록
        Book book1 = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();

        Book book2 = Book.builder()
                .title("JPA 프로그래밍")
                .author("박둘리")
                .isbn("9788956746432")
                .price(35000)
                .publishDate(LocalDate.of(2025, 4, 30))
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);
    }

    @Test
    @DisplayName("도서 등록 테스트")
    void testCreateBook() {
        Book newBook = Book.builder()
                .title("자바의 정석")
                .author("남궁성")
                .isbn("9788994492032")
                .price(30000)
                .publishDate(LocalDate.of(2020, 1, 1))
                .build();

        Book savedBook = bookRepository.save(newBook);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("자바의 정석");
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트")
    void testFindByIsbn() {
        Optional<Book> foundBook = bookRepository.findByIsbn("9788956746425");

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("스프링 부트 입문");
        assertThat(foundBook.get().getAuthor()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트")
    void testFindByAuthor() {
        List<Book> books = bookRepository.findByAuthor("박둘리");

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getIsbn()).isEqualTo("9788956746432");
    }

    @Test
    @DisplayName("도서 정보 수정 테스트")
    void testUpdateBook() {
        Book book = bookRepository.findByIsbn("9788956746425").orElseThrow();
        
        // 가격 및 제목 수정
        book.setPrice(32000);
        book.setTitle("스프링 부트 개정판");
        Book updatedBook = bookRepository.save(book);

        assertThat(updatedBook.getPrice()).isEqualTo(32000);
        assertThat(updatedBook.getTitle()).isEqualTo("스프링 부트 개정판");
    }

    @Test
    @DisplayName("도서 삭제 테스트")
    void testDeleteBook() {
        Book book = bookRepository.findByIsbn("9788956746432").orElseThrow();

        bookRepository.delete(book);

        Optional<Book> deletedBook = bookRepository.findByIsbn("9788956746432");
        assertThat(deletedBook).isEmpty();
    }
}