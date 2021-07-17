package com.jpmc.dao;

import com.jpmc.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {

	public Book findByIsbn(Long isbn);

}
