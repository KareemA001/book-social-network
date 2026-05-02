package com.Spring_boot_app.book_social_network.book;

import com.Spring_boot_app.book_social_network.commenfeatures.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@RequestBody @Valid BookRequest bookRequest, Authentication connectUser) {
        return ResponseEntity.ok(bookService.save(bookRequest, connectUser));
    }

    @GetMapping(path = "{book-id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable(value = "book-id") int bookId) {
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
    }

    @GetMapping(path = "/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));

    }

    @GetMapping(path = "/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping(path = "/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping(path = "/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable(value = "book-id") int bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId,connectedUser));
    }

    @PatchMapping(path = "/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable(value = "book-id") int bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.updateArchivedStatus(bookId,connectedUser));
    }

    @PostMapping(path = "/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable(value = "book-id") int bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
    }

    @PatchMapping(path = "/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBorrowedBook(
            @PathVariable(value = "book-id") int bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId, connectedUser));
    }

    @PatchMapping(path = "/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnBorrowedBook(
            @PathVariable(value = "book-id") int bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.approveReturnBorrowedBook(bookId, connectedUser));
    }
}
