package com.jpmc.service.Search;

import com.jpmc.dao.BookRepository;
import com.jpmc.dao.TagRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.entity.Tag;
import com.jpmc.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Qualifier("anyMatch")
public class SearchWithAnyMatch implements SearchStrategy {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final TagRepository tagRepository;

    public SearchWithAnyMatch(BookRepository bookRepository, BookMapper bookMapper, TagRepository tagRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.tagRepository = tagRepository;
    }

    @Override
    public Set<BookDTO> searchByTagList(List<String> tagNames) {
        Set<Long> isbnSetForAllInputTags = new HashSet<>();
        for (String tagName : tagNames) {
            Set<Long> isbnSetForTag = getSetOfIsbnForGivenTag(tagName);
            isbnSetForAllInputTags.addAll(isbnSetForTag);
        }

        Set<BookDTO> booksDto = isbnSetForAllInputTags
                .stream()
                .map(isbn -> bookRepository.findByIsbn(isbn))
                .map(book -> bookMapper.mapEntityToDto(book))
                .filter(bookDTO -> bookDTO.isPresent())
                .map(bookDTO -> bookDTO.get())
                .collect(Collectors.toSet());

        return booksDto;
    }

    private Set<Long> getSetOfIsbnForGivenTag(String tagName) {
        Set<Long> isbnSetOfTag = new HashSet<>();
        Tag tag = tagRepository.findByName(tagName);
        if (Objects.nonNull(tag)) {
            Set<Book> books = tag.getBooks();
            for (Book book : books) {
                isbnSetOfTag.add(book.getIsbn());
            }
        }
        return isbnSetOfTag;
    }

}
