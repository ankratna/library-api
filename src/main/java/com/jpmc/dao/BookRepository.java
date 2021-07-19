package com.jpmc.dao;

import com.jpmc.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface BookRepository extends CrudRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    public Book findByIsbn(Long isbn);

    public Set<Book> findAll();

    public Set<Book> findByAuthor(String author);

    public Set<Book> findByTitle(String title);

    public List<Book> findAll(Specification<Book> spec);

}
