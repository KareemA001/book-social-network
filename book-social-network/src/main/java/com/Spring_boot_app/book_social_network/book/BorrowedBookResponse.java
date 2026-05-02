package com.Spring_boot_app.book_social_network.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {

    private int id;
    private String title;
    private String authorName;
    private String ISBN;
    private double rate;
    private boolean returned;
    private boolean returnedApproved;
}
