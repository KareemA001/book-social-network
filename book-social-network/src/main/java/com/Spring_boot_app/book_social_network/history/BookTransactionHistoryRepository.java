package com.Spring_boot_app.book_social_network.history;

import com.Spring_boot_app.book_social_network.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer> {

    @Query("""
            SELECT history
            FROM BookTransactionHistory AS history
            WHERE history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory AS history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, int userId);

    @Query("""
            SELECT (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory AS history
            WHERE history.user.id = :userId
            AND history.book.id = :bookId
            AND history.returnApproved = flase
            """)
    boolean isAlreadyBorrowedByUser(int bookId, int userId);

    @Query("""
            SELECT transaction 
            FROM BookTransactionHistory AS history
            WHERE history.user.id = :userId
            AND history.book.id = :bbokId
            AND history.returned = false
            AND history.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(int bookId, int userId);

    @Query("""
            SELECT transaction 
            FROM BookTransactionHistory AS history
            WHERE history.book.owner.id = :userId
            AND history.book.id = :bbokId
            AND history.returned = true 
            AND history.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(int bookId, int id);
}
