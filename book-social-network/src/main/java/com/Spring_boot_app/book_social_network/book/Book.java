package com.Spring_boot_app.book_social_network.book;

import com.Spring_boot_app.book_social_network.commenfeatures.BaseEntity;
import com.Spring_boot_app.book_social_network.feedback.Feedback;
import com.Spring_boot_app.book_social_network.history.BookTransactionHistory;
import com.Spring_boot_app.book_social_network.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean sharable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> transactionHistories;

    @Transient
    public double getRate() {
       if (feedbacks == null || feedbacks.isEmpty()) {
           return 0.0;
       }

       var rate = this.feedbacks.stream()
               .mapToDouble(Feedback::getNote)
               .average()
               .orElse(0.0);
       double roundRate = Math.round(rate * 10.0) / 10.0;
       return roundRate;
    }
}
