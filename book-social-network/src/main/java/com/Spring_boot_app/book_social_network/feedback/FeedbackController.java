package com.Spring_boot_app.book_social_network.feedback;

import com.Spring_boot_app.book_social_network.commenfeatures.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> safeFeedback(
            @Valid @RequestBody FeedbackRequest feedbackRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.save(feedbackRequest,connectedUser));
    }

    @GetMapping(path = "/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook(
            @PathVariable(value = "book-id") int bookId,
            @RequestParam(name ="page",  defaultValue = "0", required = false) int page,
            @RequestParam(name ="size",  defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBook(bookId, page, size,connectedUser));
    }

//    @GetMapping(path = "/book/{book-id}")
//    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook(
//            @PathVariable(value = "book-id") int bookId,
//            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
//            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
//            Authentication connectedUser) {
//        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBook())
//    }
//    )
}

