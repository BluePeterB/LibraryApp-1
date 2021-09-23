package com.sertaperdana.libraryApp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity  //For persistence. Hibernate scans this app looking for @Entity. Then creates in SQL a table ...
public class Author {   // called author (if not already present) and adds a column for each field below.
    // this was enabled by the setting in application.properties :-
    // spring.jpa.hibernate.ddl-auto = update


    @NotBlank(message = "Birthday is required")
    @Size(min = 5, max = 10, message = "Birthday must be between 5 and 10 characters")
    private String birthday;

    @NotBlank(message = "deathday is required")
    @Size(min = 5, max = 10, message = "deathday must be between 5 and 10 characters")
    private String deathday;

    @NotBlank(message = "First Name is required")
    @Size(min = 3, max = 30, message = "First Name must be between 3 and 30 characters")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 3, max = 30, message = "Last Name must be between 3 and 30 characters")
    private String lastName;

    // CONSTRUCTOR
    public Author(String birthday, String deathday, String firstName, String lastName) {
        this();
        this.birthday = birthday;
        this.deathday = deathday;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Id   //For persistence
    @GeneratedValue   //For persistence lets DB update the ID*
    private int authorId;

    //constructor for persistence
    public Author() {
    }

    public String getDeathday() {

        System.out.println("deathday: " + deathday);
        return deathday;
    }

    // Method 'changeDateFormat' (at bottom) is used to display 1984//08/16 as 08/16/1984.
    public String getFormattedDeathday() {
        return changeDateFormat(deathday);
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getFormattedBirthday() {
        return changeDateFormat(birthday);
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first_name) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getSortableAuthor() {
        return lastName + " " + firstName;
    }


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return authorId == author.authorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }


    public static String changeDateFormat(String date) {  //e.g. 1942-03-12
        System.out.println("date date date: " + date);
        if (date.equals("NULL")) {return "------";}  //handles apparent confusion with keyword 'null'
        int length = date.length();
        char chr = date.charAt(4);
        if (length != 10 || chr != '-') {
            return date;
        }
        String year = date.substring(0, 4); //index starts at 0. 2nd index = one past end!!!
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        date =  month + "/" + day + "/" + year;  //e.g  '1982-11-18' is changed to '11/18/1982'
        return date;
    }
}