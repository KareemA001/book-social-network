package com.Spring_boot_app.book_social_network.feedback;

import com.Spring_boot_app.book_social_network.book.Book;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class FeedbackMapper {
    public Feedback toFeedback(@Valid FeedbackRequest feedbackRequest) {

        return Feedback.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .book(Book.builder()
                        .id(feedbackRequest.bookId())
                        .archived(false)
                        .sharable(false)
                        .build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, int id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(),id))
                .build();
    }
}
