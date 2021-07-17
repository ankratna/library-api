package com.jpmc.domainobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "book")
public class BookDO {

    @Id
    @NotNull(message = "isbn number can not be null")
    private Long isbn;

    @Column(nullable = false)
    @NotNull(message = "title of book can not be null")
    private String title;

    @Column(nullable = false)
    @NotNull(message = "author of can not be null")
    private String author;

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

    private BookDO() {
    }

    public static BookDOBuilder newBuilder(){return new BookDOBuilder();}

    public static final class BookDOBuilder {
        private Long isbn;
        private String title;
        private String author;

        private BookDOBuilder() {
        }

        public static BookDOBuilder BookDO() {
            return new BookDOBuilder();
        }

        public BookDOBuilder withIsbn(Long isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookDOBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public BookDOBuilder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookDO build() {
            BookDO bookDO = new BookDO();
            bookDO.setIsbn(isbn);
            bookDO.setTitle(title);
            bookDO.setAuthor(author);
            return bookDO;
        }
    }
}
