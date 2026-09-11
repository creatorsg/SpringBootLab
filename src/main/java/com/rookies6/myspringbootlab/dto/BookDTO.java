package com.rookies6.myspringbootlab.dto;

import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.entity.BookDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    // 1. 등록 / 전체 수정용 Request
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "저자는 필수입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 양수이어야 합니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수입니다.")
        @PastOrPresent(message = "출판일은 미래 날짜일 수 없습니다.")
        private LocalDate publishDate;

        @Valid
        private BookDetailDTO detailRequest;
    }

    // 2. BookDetail 중첩 DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailDTO {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    // 3. Book PATCH 용 DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PatchRequest {
        private String title;
        private String author;
        private String isbn;

        @Positive(message = "가격은 양수이어야 합니다.")
        private Integer price;

        @PastOrPresent(message = "출판일은 미래 날짜일 수 없습니다.")
        private LocalDate publishDate;

        private BookDetailDTO detailRequest;
    }

    // 4. BookDetail 전용 PATCH 용 DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailPatchRequest {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    // 5. 응답용 Response DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        public static Response fromEntity(Book book) {
            BookDetailResponse detailResponse = null;
            if (book.getBookDetail() != null) {
                BookDetail bd = book.getBookDetail();
                detailResponse = BookDetailResponse.builder()
                        .id(bd.getId())
                        .description(bd.getDescription())
                        .language(bd.getLanguage())
                        .pageCount(bd.getPageCount())
                        .publisher(bd.getPublisher())
                        .coverImageUrl(bd.getCoverImageUrl())
                        .edition(bd.getEdition())
                        .build();
            }

            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailResponse)
                    .build();
        }
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}