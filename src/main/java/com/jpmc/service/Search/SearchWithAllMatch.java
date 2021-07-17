package com.jpmc.service.Search;

import com.jpmc.dao.BookRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.mapper.BookMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchWithAllMatch implements SearchStrategy {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public SearchWithAllMatch(BookRepository bookRepository, BookMapper bookMapper){
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public Set<BookDTO> searchByTagList(List<String> tags){
        Set<String> tagsSet = new HashSet<>(tags);
        return bookRepository.findAll()
                .stream()
                .filter(book->getTagSet(book).containsAll(tags))
                .map(book->bookMapper.mapEntityToDto(book))
                .collect(Collectors.toSet());
    }

    private Set<String> getTagSet(Book book){
        return book.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toSet());
    }

}
