package com.sertaperdana.libraryApp.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;


@Entity  //For persistence. Hibernate scans this app looking for @Entity. Then creates in SQL a table ...
public class Loan {   // called Loan (if not already present) and adds a column for each field below.
    // this was enabled by the setting in application.properties :-
    // spring.jpa.hibernate.ddl-auto = update

    // private int bookId;
    @ManyToOne
    private Book book;


    private String dateIn;

    public String dateOut;

    //private int patronId;
    @ManyToOne
    private Patron patron;



    //CONSTRUCTOR
    public Loan(Book book, String dateIn,  String dateOut, Patron patron) {  //int patronId) {
        this();
        //this.bookId = bookkId;
        this.book = book;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        //this.patronId = patronId;
        this.patron = patron;
    }

    //AUTO GENERATED ID
    @Id   //For persistence
    @GeneratedValue   //For persistence lets DB update the ID*
    private int loanId;

    //CONSTRUCTOR FOR PERSISTENCE
    public Loan() { }


    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }



    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public String getSortableDateIn() {  // See getSortableDate(date) last below.
        return getSortableDate(dateIn);
    }

    public String getSortableDateOut() {
        return getSortableDate(dateOut);
    }



    public String getDateOut() {
        return dateOut;
    }

    public void setDateOut(String dateOut) {
        this.dateOut = dateOut;
    }

    // public int getBookId() { return bookId; }
    //public void setBookId(int bookId) { this.bookId = bookId; }
    public Book getBook() {return book;}
    public void setBook(Book book) { this.book = book;}

    public String getSortableBook() {
        String modTitle = book.getTitle();
        if (modTitle.startsWith("A ") ||modTitle.startsWith("An ") || modTitle.startsWith("The ")) {

            modTitle = modTitle.replaceFirst("The ", "");
            modTitle = modTitle.replaceFirst("A ", "");
            modTitle = modTitle.replaceFirst("An ", "");
            //System.out.println(modTitle);
        }
        return modTitle;
    }

    // public int getPatronId() { return patronId;  }
    //public void setPatron(int patronId) { this.patronId = patronId;}
    public Patron getPatron() {return patron;}
    public void setPatron(Patron patron) { this.patron = patron;}

    public String getSortablePatron() {
        return patron.getLastName() + " " + patron.getFirstName();
    }

    @Override
    public String toString() {
        return dateIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return loanId == loan.loanId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }

    public static String getSortableDate(String date) {
        int length = date.length();
        if(length < 10){return "99999999";}
        String year = date.substring(length - 4, length); //index starts at 0. 2nd index = one past end!!!
        String monthDay = date.substring(0, length-5);
        date = year + "/" + monthDay;  //e.g '11/18/1982' is changed to '1982/11/18'.
        return date;
    }


}

