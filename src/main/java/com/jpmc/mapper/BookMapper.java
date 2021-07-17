package com.jpmc.mapper;


import com.jpmc.datatransferobject.BookDTO;
import com.jpmc.domainobject.BookDO;

public class BookMapper {
    public static BookDO makeBookDO(BookDTO bookDTO)
    {
        return BookDO.newBuilder()
                .withIsbn(bookDTO.getIsbn())
                .withAuthor(bookDTO.getAuthor())
                .withTitle(bookDTO.getTitle())
                .build();
    }
}
