package com.jpmc.service.impl;

import com.jpmc.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecification {

	public Specification<Book> findWithTitle(String title) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title);
	}

	public Specification<Book> findWithIsbn(Long isbn) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isbn"), isbn);
	}

	public Specification<Book> findWithAuthor(String author) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("author"), author);
	}

}
