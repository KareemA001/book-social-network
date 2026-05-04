package com.Spring_boot_app.book_social_network.feedback;


import com.Spring_boot_app.book_social_network.book.Book;
import com.Spring_boot_app.book_social_network.book.BookRepository;
import com.Spring_boot_app.book_social_network.commenfeatures.PageResponse;
import com.Spring_boot_app.book_social_network.exception.OperationNotPermittedException;
import com.Spring_boot_app.book_social_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;
    public Integer save(@Valid FeedbackRequest feedbackRequest, Authentication connectedUser) {

        Book returnedBook = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(() -> new EntityNotFoundException("The book is not existed"));

        if (returnedBook.isArchived() || !returnedBook.isSharable()) {
            throw new OperationNotPermittedException("The book is archived, you can't give a feedback for such a book");
        }
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(returnedBook.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't give a feedback to your own book");
        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return  feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(int bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page,size);
        User user = (User)connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId,pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f,user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
