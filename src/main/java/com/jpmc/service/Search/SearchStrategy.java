package com.jpmc.service.Search;

import com.jpmc.dto.BookDTO;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface SearchStrategy {

	public Set<BookDTO> searchByTagList(List<String> tags);

}
