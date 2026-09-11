package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.dto.BookDTO;
import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.entity.BookDetail;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.repository.BookDetailRepository;
import com.rookies6.myspringbootlab.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public BookService(BookRepository bookRepository, BookDetailRepository bookDetailRepository) {
        this.bookRepository = bookRepository;
        this.bookDetailRepository = bookDetailRepository;
    }

    // 생성
    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 존재하는 ISBN입니다: " + request.getIsbn(), HttpStatus.BAD_REQUEST);
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        if (request.getDetailRequest() != null) {
            BookDTO.BookDetailDTO detailDto = request.getDetailRequest();
            BookDetail bookDetail = BookDetail.builder()
                    .description(detailDto.getDescription())
                    .language(detailDto.getLanguage())
                    .pageCount(detailDto.getPageCount())
                    .publisher(detailDto.getPublisher())
                    .coverImageUrl(detailDto.getCoverImageUrl())
                    .edition(detailDto.getEdition())
                    .build();

            book.setBookDetail(bookDetail);
        }

        Book savedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(savedBook);
    }

    // 조회 기능들
    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다: " + isbn, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author).stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookDTO.Response> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title).stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    // 전체 수정 (PUT)
    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        // ISBN 검증
        if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 존재하여 사용할 수 없는 ISBN입니다: " + request.getIsbn(), HttpStatus.BAD_REQUEST);
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null) {
            BookDTO.BookDetailDTO detailDto = request.getDetailRequest();
            if (book.getBookDetail() == null) {
                book.setBookDetail(new BookDetail());
            }
            BookDetail detail = book.getBookDetail();
            detail.setDescription(detailDto.getDescription());
            detail.setLanguage(detailDto.getLanguage());
            detail.setPageCount(detailDto.getPageCount());
            detail.setPublisher(detailDto.getPublisher());
            detail.setCoverImageUrl(detailDto.getCoverImageUrl());
            detail.setEdition(detailDto.getEdition());
        }

        return BookDTO.Response.fromEntity(book);
    }

    // 부분 수정 (PATCH - Book 및 선택적 BookDetail)
    @Transactional
    public BookDTO.Response patchBook(Long id, BookDTO.PatchRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        if (request.getIsbn() != null && !book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 존재하여 사용할 수 없는 ISBN입니다: " + request.getIsbn(), HttpStatus.BAD_REQUEST);
        }

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getPublishDate() != null) book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null) {
            if (book.getBookDetail() == null) {
                book.setBookDetail(new BookDetail());
            }
            BookDetail detail = book.getBookDetail();
            BookDTO.BookDetailDTO detailDto = request.getDetailRequest();

            if (detailDto.getDescription() != null) detail.setDescription(detailDto.getDescription());
            if (detailDto.getLanguage() != null) detail.setLanguage(detailDto.getLanguage());
            if (detailDto.getPageCount() != null) detail.setPageCount(detailDto.getPageCount());
            if (detailDto.getPublisher() != null) detail.setPublisher(detailDto.getPublisher());
            if (detailDto.getCoverImageUrl() != null) detail.setCoverImageUrl(detailDto.getCoverImageUrl());
            if (detailDto.getEdition() != null) detail.setEdition(detailDto.getEdition());
        }

        return BookDTO.Response.fromEntity(book);
    }

    // 부분 수정 (PATCH - BookDetail 단독)
    @Transactional
    public BookDTO.Response patchBookDetail(Long id, BookDTO.BookDetailPatchRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        if (book.getBookDetail() == null) {
            book.setBookDetail(new BookDetail());
        }
        BookDetail detail = book.getBookDetail();

        if (request.getDescription() != null) detail.setDescription(request.getDescription());
        if (request.getLanguage() != null) detail.setLanguage(request.getLanguage());
        if (request.getPageCount() != null) detail.setPageCount(request.getPageCount());
        if (request.getPublisher() != null) detail.setPublisher(request.getPublisher());
        if (request.getCoverImageUrl() != null) detail.setCoverImageUrl(request.getCoverImageUrl());
        if (request.getEdition() != null) detail.setEdition(request.getEdition());

        return BookDTO.Response.fromEntity(book);
    }

    // 삭제
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}