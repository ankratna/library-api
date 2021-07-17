package com.jpmc.datatransferobject;


import javax.validation.constraints.NotNull;
import java.util.List;

public class BookDTO {

    @NotNull(message = "isbn number can not be null")
    private Long isbn;

    @NotNull(message = "title of book can not be null")
    private String title;

    @NotNull(message = "author of can not be null")
    private String author;

    private List<String> tags;

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private BookDTO() {
    }


    public static final class BookDTOBuilder {
        private Long isbn;
        private String title;
        private String author;
        private List<String> tags;

        private BookDTOBuilder() {
        }

        public static BookDTOBuilder BookDTO() {
            return new BookDTOBuilder();
        }

        public BookDTOBuilder withIsbn(Long isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookDTOBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public BookDTOBuilder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookDTOBuilder withTags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public BookDTO build() {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setIsbn(isbn);
            bookDTO.setTitle(title);
            bookDTO.setAuthor(author);
            bookDTO.setTags(tags);
            return bookDTO;
        }
    }
}
