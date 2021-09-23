package com.sertaperdana.libraryApp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity  //For persistence. Hibernate scans this app looking for @Entity. Then creates in SQL a table ...
public class Patron {   // called patron (if not already present) and adds a column for each field below.
    // this was enabled by the setting in application.properties :-
    // spring.jpa.hibernate.ddl-auto = update



    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 30, message = "First name must be between 3 and 30 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 30, message = "Last name must be between 3 and 30 characters")
    private String lastName;

    @NotBlank(message = "Address is required")
    @Size(min = 3, max = 50, message = "Address must be between 3 and 50 characters")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Size(min = 3, max = 15, message = "Phone Number must be between 3 and 15 characters")
    private String phoneNum;

    // CONSTRUCTOR
    public Patron(String firstName, String lastName, String address, String phoneNum) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNum = phoneNum;
    }

    @Id   //For persistence
    @GeneratedValue   //For persistence lets DB update the ID*
    private int patronId;

    //constructor for persistence
    public Patron() {
    }  //needed for ??

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        if(phoneNum == null) {return "-------";}
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return patronId == patron.patronId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }

}
