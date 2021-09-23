package com.sertaperdana.libraryApp.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity  //For persistence. Hibernate scans this app looking for @Entity. Then creates in SQL a table ...
public class Book {   // called book (if not already present) and adds a column for each field below.
    // this was enabled by the setting in application.properties :-
    // spring.jpa.hibernate.ddl-auto = update


    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    //private int authorId;
    @ManyToOne
    private Author author;

    public String isbn;

    private int available;

    @ManyToOne
    private Genre genre;



    public Book(String title, Author author,  String isbn, int available, Genre genre) {
        this();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
        this.genre = genre;
    }

    @Id   //For persistence
    @GeneratedValue   //For persistence lets DB update the ID*
    private int bookId;


    public Book() { }  //needed for ??


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) { this.author = author;}

    public String getTitle() {
        return title;
    }

    public String getSortableTitle() {
        String modTitle = title;
        if (modTitle.startsWith("A ") ||modTitle.startsWith("An ") || modTitle.startsWith("The ")) {

            modTitle = modTitle.replaceFirst("The ", "");
            modTitle = modTitle.replaceFirst("A ", "");
            modTitle = modTitle.replaceFirst("An ", "");
            //System.out.println(modTitle);
        }
        return modTitle;
    }



    public String getSortableAuthor() {
        return author.getLastName() + " " +author.getFirstName();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }





    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) { this.genre = genre;}

    public String getAvailability() {
        // System.out.println("Available: " + available);
        if ( available == 0) {
            return "Available";
        }else if(available == 1) {
            return "NOT AVAILABLE";
        }else if(available == 2) {
            return "LIBRARY USE ONLY";
        } else
            return  "ERROR";
    }


    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}
