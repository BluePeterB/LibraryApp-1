package com.sertaperdana.libraryApp.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity  //For persistence. Hibernate scans this app looking for @Entity. Then creates in SQL a table ...
public class Genre {   // called genre (if not already present) and adds a column for each field below.
    // this was enabled by the setting in application.properties :-
    // spring.jpa.hibernate.ddl-auto = update



    @NotBlank(message = "Genre Type is required")
    @Size(min = 5, max = 30, message = "Genre Type must be between 5 and 30 characters")
    private String genreType;  // Creates a column called genre_type in SQL


    // CONSTRUCTOR
    public Genre(String genreType) {
        this();
        this.genreType = genreType;
    }

    @Id   //For persistence
    @GeneratedValue   //For persistence lets DB update the ID*
    private int genreId;

    //constructor for persistence
    public Genre() {
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreType() {
        return genreType;
    }

    public void setGenreType(String genreType) {
        this.genreType = genreType;
    }



    @Override
    public String toString() {
        return genreType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return genreId == genre.genreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId);
    }

}

