package com.Spring_boot_app.book_social_network.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {

    @Query("""
            SELECT feedback
            FROM Feedback AS feedback
            WHERE feedback.book.id = :bookId
            """)
    Page<Feedback> findAllByBookId(int bookId, Pageable pageable);
}
