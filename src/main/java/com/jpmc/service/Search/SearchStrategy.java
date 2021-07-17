package com.jpmc.service.Search;

import com.jpmc.dto.BookDTO;

import java.util.List;
import java.util.Set;

public interface SearchStrategy {

    public Set<BookDTO> searchByTagList(List<String> tags);
}
