package com.Spring_boot_app.book_social_network.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private int id;
    private String title;
    private String authorName;
    private String ISBN;
    private String synopsis;
    private String owner;
    private byte[] cover;
    private double rate;
    private boolean archived;
    private boolean sharable;
}
