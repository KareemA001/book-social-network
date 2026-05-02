package com.Spring_boot_app.book_social_network.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> withOwnerId(int ownerId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId));
    }
}
