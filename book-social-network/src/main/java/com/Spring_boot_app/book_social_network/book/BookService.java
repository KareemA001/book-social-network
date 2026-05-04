package com.Spring_boot_app.book_social_network.book;

import com.Spring_boot_app.book_social_network.commenfeatures.PageResponse;
import com.Spring_boot_app.book_social_network.exception.OperationNotPermittedException;
import com.Spring_boot_app.book_social_network.file.FileStorageService;
import com.Spring_boot_app.book_social_network.history.BookTransactionHistory;
import com.Spring_boot_app.book_social_network.history.BookTransactionHistoryRepository;
import com.Spring_boot_app.book_social_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.Spring_boot_app.book_social_network.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;
    public Integer save(BookRequest bookRequest, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(int bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with this ID: "+bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()),pageable);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> borrowedBooks = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                borrowedBooks,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> borrowedBooks = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                borrowedBooks,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(int bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with ID: "+ bookId));
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't update the book shareable status");
        }
        book.setSharable(!book.isSharable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(int bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with ID: "+ bookId));
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't update the book archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(int bookId, Authentication connectedUser) {

        Book returnedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("This book is not available"));
        if (returnedBook.isArchived() || !returnedBook.isSharable()) {
            throw new OperationNotPermittedException("The requested book is not available");
        }
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(returnedBook.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't borrow your own book");
        }
        final boolean isBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isBorrowed) {
            throw new OperationNotPermittedException("The book is already borrowed try later");
        }
        BookTransactionHistory transactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(returnedBook)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(transactionHistory).getId();
    }

    public Integer returnBorrowedBook(int bookId, Authentication connectedUser) {
        Book returnedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("This book is not available"));

        if (returnedBook.isArchived() || !returnedBook.isSharable()) {
            throw new OperationNotPermittedException("The requested book is not available");
        }
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(returnedBook.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't borrow or return your own book");
        }
        BookTransactionHistory transactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You didn't borrow this book"));
        transactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(transactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(int bookId, Authentication connectedUser) {

        Book returnedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("This book is not available"));

        if (returnedBook.isArchived() || !returnedBook.isSharable()) {
            throw new OperationNotPermittedException("The requested book is not available");
        }
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(returnedBook.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't borrow or return your own book");
        }
        BookTransactionHistory transactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not approved yet"));
        transactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(transactionHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, int bookId) {
        Book requiredBook = bookRepository.findById(bookId)
                        .orElseThrow(() -> new EntityNotFoundException("The required book is not existed"));
        User user = (User) connectedUser.getPrincipal();
        var bookCover = fileStorageService.saveFile(file, user.getId());
        requiredBook.setBookCover(bookCover);
        bookRepository.save(requiredBook);
    }
}
