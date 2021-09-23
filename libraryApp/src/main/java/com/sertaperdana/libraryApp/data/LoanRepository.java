package com.sertaperdana.libraryApp.data;


import com.sertaperdana.libraryApp.models.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository  //Signifies to SpringBoot that this class will store objects in the DB.
public interface LoanRepository extends CrudRepository<Loan, Integer> {
    // This interface class was added / is needed for persistence.
    // <Loan, for type of thing we are storing.
    // Integer> for data type of the primary key for that class.
    Loan findByLoanId(int loanId);
}