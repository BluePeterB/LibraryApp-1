package com.sertaperdana.libraryApp.controllers;

import com.sertaperdana.libraryApp.data.*;
import com.sertaperdana.libraryApp.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;


@Controller
@RequestMapping("library")
public class BookController {

    private static int savedBookId = 0;
    private static int savedPatronId = 0;
    //private static boolean savedLoanR = 0;
    private static boolean savedLoan = false;
    private static String savedBookSort = "title";
    private static String savedLoanSort = "title";
    private static Object theBook = null;
    private static Object thePatron = null;
    private static boolean savedLoanR = false;
    private static boolean savedNewBook = false;
    private static String savedNewTitle = "";
    private static String savedNewIsbn = "";
    private static int savedAuthorId = 0;
    private static int savedGenreId = 0;

    // @Autowired  //Specifies that SpringBoot should auto-populate the eventRepository field (dependency injection).
    // private BookRepository bookRepository;  //Added for persistence

    @Autowired  //Specifies that SpringBoot should auto-populate the eventRepository field (dependency injection).
    private PatronRepository patronRepository;  //Added for persistence

    @Autowired  //Specifies that SpringBoot should auto-populate the eventRepository field (dependency injection).
    private BookRepository bookRepository;  //Added for persistence

    @Autowired  //Specifies that SpringBoot should auto-populate the eventRepository field (dependency injection).
    private LoanRepository loanRepository;  //Added for persistence

    @Autowired  //Specifies that SpringBoot should auto-populate the eventRepository field (dependency injection).
    private AuthorRepository authorRepository;  //Added for persistence

    @Autowired  //Specifies that SpringBoot should auto-populate the eventRepository field (dependency injection).
    private GenreRepository genreRepository;  //Added for persistence


    @GetMapping("book") //  responds to a browser Get request to /library/book OR ditto from fragments
    public String displayAllBooks(@RequestParam(required = false) boolean loan, String sort, Model model) {
        System.out.println("InGetMappingBook, loan = " + loan);
        System.out.println("$$$$$$$$$ sort: " + sort);

        //Below 2 lines set up 'order by title' in the case of no sort request yet.
        String sortType = "";
        //model.addAttribute("orderBy", Comparator.comparing(Book::getSortableTitle));

        if (sort == null) {   // If not a sort request then keep the last sort status.
            sort = savedBookSort;
        } else {
            loan = savedLoan;   // if this is a sort request then keep last loan status
            // for when window is redrawn with new sort type.
        }
        if (sort.equals("title")) {
            sortType = "Title";   //Save sort type for display and (line below) set comparator type.
            model.addAttribute("orderBy", Comparator.comparing(Book::getSortableTitle));
        } else if (sort.equals("author")) {
            sortType = "Author";
            model.addAttribute("orderBy", Comparator.comparing(Book::getSortableAuthor));
        } else if (sort.equals("isbn")) {
            sortType = "ISBN";
            model.addAttribute("orderBy", Comparator.comparing(Book::getIsbn));
        } else if (sort.equals("bookId")) {
            sortType = "Ref Number";
            model.addAttribute("orderBy", Comparator.comparing(Book::getBookId));
        }

        savedLoan = loan;  // for use in patron & loan which are called if loan is true.
        savedBookSort = sort;  // To retain the last requested sort configuration.

        String title = "All Books"; // set title for the non-loan case.
        if (loan) {
            title = "New Loan";
            model.addAttribute("instruction", "Select book to be loaned");
        }
        //System.out.println("$$$$$$$$$ books: " + bookRepository.findAll());
        model.addAttribute("title", title);
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("loan", loan);
        model.addAttribute("orderedBy", sortType);
        return "library/book";  //calls book template
    }

    @PostMapping("book")
    public String processBookSelectForm(@RequestParam(required = false) int bookIdX) {
        System.out.println("InPostMappingBook");
        savedBookId = bookIdX;
        System.out.println("####savedBookId: " + savedBookId);
        return "redirect:patron";  //calls GetMapping("patron") below
    }


    @GetMapping("patron")  //  responds to a browser Get request to /library/patron.
    public String displayAllPatrons(Model model) {
        System.out.println("InGetMappingPatron, loan = " + savedLoan);  //debug
        //System.out.println("####savedPatronId: " + savedPatronId);
        String title = "All Patrons";
        if (savedLoan) {
            title = "New Loan";
            model.addAttribute("instruction", "Select the patron for this loan");
        }
        model.addAttribute("title", title);

        model.addAttribute("patrons", patronRepository.findAll());
        model.addAttribute("loan", savedLoan);
        model.addAttribute("byLastName", Comparator.comparing(Patron::getLastName));
        return "library/patron";  //calls the patron template
    }


    @PostMapping("patron")
    // @ResponseBody
    public String processPatronSelectForm(@RequestParam(required = false) int patronId, Model model) {
        //public String processPatronSelectForm(Model model) {
        System.out.println("InPostMappingPatron");
        savedPatronId = patronId;
        System.out.println("####savedPatronId: " + savedPatronId);
        System.out.println("####bookM: " + bookRepository.findById(savedBookId));
        Optional bookResult = bookRepository.findById(savedBookId);
        Optional patronResult = patronRepository.findById(savedPatronId);
        theBook = bookResult.get();
        thePatron = patronResult.get();
        model.addAttribute("title", "New Loan");
        model.addAttribute("theBook", theBook);
        model.addAttribute("thePatron", thePatron);
        //boolean savedNewLoan = false;
        model.addAttribute("confirmed", false);
        return "library/newLoan";   //calls template newLoan
    }

    @GetMapping("newPatron")  //  responds to a browser Get request to /library/patron.
    public String displayNewPatron(Model model) {
        System.out.println("InGetMappingNewPatron");  //debug
         String title = "New Patron";
         model.addAttribute("instruction", "Enter the new patron");

        model.addAttribute("title", title);


        return "library/newPatron";  //calls the newPatron template
    }

    @PostMapping("newPatron")
    public String processNewPatronForm(@RequestParam(required = true) String firstName,
                                         @RequestParam(required = true) String lastName,
                                       @RequestParam(defaultValue = "-----") String address,
                                       @RequestParam(defaultValue = "-----") String phoneNum) {
        System.out.println("InPostMappingNewPatron");

        Patron newPtn = new Patron(firstName, lastName, address, phoneNum);
       patronRepository.save(newPtn);
        return "redirect:patron";  //calls GetMapping("patron")
    }



    @PostMapping("newLoan")  //Entry from postMappingPatron above.
    public String processNewLoanForm(@RequestParam(required = false) boolean confirmed, Model model) {
        System.out.println("InPostMappingNewLoan: " + confirmed);
        //System.out.println("savedNewLoan: " + savedNewLoan);
        if (confirmed != true) {   //If new loan cancelled
            return "redirect:book?loan=true";
        } else {
            // New loan confirmed so get today's date
            LocalDateTime ldt = LocalDateTime.now();
            // Custom format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");  // HH:mm:ss");
            // Format LocalDateTime
            String dateOUT = ldt.format(formatter);

            //System.out.println("dateOUT: " + dateOUT);


            Optional bookResult = bookRepository.findById(savedBookId);
            //theBook = bookResult.get();
            Book thisBook = (Book) bookResult.get();
            Optional patronResult = patronRepository.findById(savedPatronId);
            //thePatron = patronResult.get();
            Patron thisPatron = (Patron) patronResult.get();
            Loan newLn = new Loan(thisBook, "", dateOUT, thisPatron);
            loanRepository.save(newLn);

            thisBook.setAvailable(1); // set to unavailable, 0 = available
            //System.out.println("******theBook: " + thisBook.getAvailable());
            bookRepository.save(thisBook);

            return "redirect:loan";  //calls GetMapping("loan") below
        }
    }

    @GetMapping("loan")  //  responds to a browser Get request to /library/loan.
    public String displayAllLoans(@RequestParam(required = false) boolean loanR, String sort, Model model) {
        System.out.println("InGetMappingDisplayAllLoans. loanR : " + loanR);  //debug

        String sortType = "";

        if (sort == null) {   // If not a sort request then keep the last sort status.
            sort = savedLoanSort;
        } else {
            loanR = savedLoanR;   // if this is a sort request then keep last loan status
            // for when window is redrawn with new sort type.
        }
        if (sort.equals("title")) {
            sortType = "Title";   //Save sort type for display and set comparator type.
            model.addAttribute("orderBy", Comparator.comparing(Loan::getSortableBook));
        } else if (sort.equals("patron")) {
            sortType = "Patron";
            model.addAttribute("orderBy", Comparator.comparing(Loan::getSortablePatron));
        } else if (sort.equals("dateOut")) {
            sortType = "Date Out";
            model.addAttribute("orderBy", Comparator.comparing(Loan::getSortableDateOut));
        } else if (sort.equals("dateIn")) {
            sortType = "Date In";
            model.addAttribute("orderBy", Comparator.comparing(Loan::getSortableDateIn));
        }

        savedLoanR = loanR;  // for use in patron & loan which are called if loan is true.
        savedLoanSort = sort;  // To retain the last requested sort configuration.

        String title = "All Loans";  // Used by fragments.html which is called by the loan template.
        if (loanR) {
            title = "Loan Return";
            model.addAttribute("instruction", "Select the loan that is being returned");
        }
        model.addAttribute("title", "All Loans");
        model.addAttribute("loans", loanRepository.findAll());
        model.addAttribute("loanR", loanR);
        model.addAttribute("title", title);
        model.addAttribute("sortType", sortType);
        return "library/loan";  //calls template loan
    }

    @PostMapping("loan")
    public String processLoanForm(@RequestParam(required = false) int loanIdX) {
        System.out.println("InPostMappingLoan");
        System.out.println("##############loanR: " + loanIdX);
        if (loanIdX == 0) {
            return "redirect:loan";  //calls GetMapping loan with no action
        }

        //**Get the loan for this loanIdX**
        Loan thisLoan = loanRepository.findByLoanId(loanIdX);
        // The above line uses the loanRepository method 'findByLoanId' to avoid an optional result.
        // Get the bookId corresponding to thisLoan
        int thisBookId = thisLoan.getBook().getBookId();

        System.out.println("##############BookId: " + thisLoan.getBook().getBookId());

        //Get today's date
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");  // HH:mm:ss");
        String dateNow = ldt.format(formatter);
        // **Set the 'loan' table dateIn column to today's date**
        //thisLoan.setDateIn(dateNow); // set to current date
        thisLoan.setDateIn(dateNow); // set to current date
        loanRepository.save(thisLoan);
        //**Set the 'book' table column 'available' to zero meaning 'now available'.**
        Book thisBook = bookRepository.findByBookId(thisBookId);
        thisBook.setAvailable(0);
        bookRepository.save(thisBook);

        return "redirect:loan";  //calls GetMapping loan
    }

    @PostMapping("loanReturn")  //Entry from template loan post  with loanIdX set to required loan.
    public String processLoanReturnForm(@RequestParam(required = true) int loanIdX, Model model) {
        System.out.println("InPostMappingLoanReturn. loanIdX: " + loanIdX);
        Loan thisLoan = loanRepository.findByLoanId(loanIdX);
        model.addAttribute("thisLoan", thisLoan);
        model.addAttribute("title", "Loan Return");
        return "library/loanReturn";   //calls template loanReturn
    }


    @GetMapping("author")  //  responds to a browser Get request to /library/author.
    public String displayAllAuthors(@RequestParam(required = true) boolean newBook, Model model) {
        System.out.println("InGetMappingDisplayAllAuthors");  //debug
        String tempTitle = "All Authors";
        if (newBook) {
            tempTitle = "Enter Author";
            model.addAttribute("instruction", "Enter a new or select an existing author.");
        }
        model.addAttribute("orderBy", Comparator.comparing(Author::getSortableAuthor));
        model.addAttribute("title", tempTitle);
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("newBook", newBook);
        return "library/author";
    }

    @GetMapping("genre")  //  responds to a browser Get request to /library/genre.
    public String displayAllGenre(@RequestParam(required = true) boolean newBook, Model model) {
        System.out.println("InGetMappingDisplayAllGenre");  //debug
        model.addAttribute("newBook",newBook);
        String tempTitle = "All Genres"; // set title for the non-loan case.
        if (newBook) {
            tempTitle = "Enter Genre";
            model.addAttribute("instruction", "Select the genre for this book.");
        }
        model.addAttribute("title", tempTitle);
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("orderBy", Comparator.comparing(Genre::getGenreType));
        return "library/genre";
    }

    @GetMapping("newBook")  //  responds to a browser Get request to /library/newBook
    public String displayNewBook(@RequestParam int genreIdX, Model model) {
        System.out.println("InGetMappingDisplayNewBook");  //debug
        Author thisAuthor = authorRepository.findByAuthorId(savedAuthorId);
        String authorName = thisAuthor.getFirstName() + "" + thisAuthor.getLastName();
        Genre thisGenre = genreRepository.findByGenreId(genreIdX);
        savedGenreId = genreIdX;
        System.out.println("authorName: " + authorName);
        model.addAttribute("author", authorName);
        model.addAttribute("genre", thisGenre.getGenreType());
        savedNewBook = true;
        return "library/newBook";
    }

    @PostMapping("newBook")  //  responds to a browser Post request to /library/newBook.
    public String processNewBookForm(@RequestParam String result,
                                     @RequestParam(defaultValue = "") String title,
                                     @RequestParam(defaultValue = "") String isbn, Model model) {
        System.out.println("InPostMappingNewBook");  //debug
        System.out.println("result:" + result);  //debug
        System.out.println("title:" + title);  //debug
        System.out.println("isbn:" + isbn);  //debug
        System.out.println("savedAuthorId:" + savedAuthorId);  //debug
        System.out.println("savedGenreId:" + savedGenreId);  //debug
        Author thisAuthor = authorRepository.findByAuthorId(savedAuthorId);
        Genre thisGenre = genreRepository.findByGenreId(savedGenreId);
        if(result.equals("Add Book")){
            Book newBk = new Book(title, thisAuthor, isbn, 0, thisGenre);
            bookRepository.save(newBk);
            return "redirect:book";  //calls GetMapping book to show new book.
        }else{
            return "redirect:newBook";  //calls GetMapping newBook to try again maybe.
        }


    }

    @PostMapping("addTitle")  //  responds to a browser Get request to /library/addTitle.
    public String processAddTitleForm(@RequestParam(required = true) String title, String isbn, Model model) {
        System.out.println("InPostMappingProcessAddTitleForm");  //debug
        //model.addAttribute("title", title);
        model.addAttribute("newBook", true);
        savedNewBook = true;
        savedNewTitle = title;
        savedNewIsbn = isbn;
        System.out.println("&&&&&&&& savednewBook: " + savedNewBook);
        System.out.println("&&&&&&&& savedNewTitle: " + savedNewTitle);
        System.out.println("&&&&&&&& savedNewIsbn: " + savedNewIsbn);

        //return "library/author";  // calls author template directly, not required here.
        return "redirect:book";  //calls GetMapping author
    }

    @PostMapping("addAuthor")  //  responds to a browser Post request to /library/addAuthor.
    public String processAddAuthor(@RequestParam(required = false)
                                               int authorIdX, String firstName, String lastName,
                                   @RequestParam(defaultValue = "-----")String birthday,
                                   @RequestParam(defaultValue = "-----") String deathday, Model model) {
        System.out.println("InPostMappingAddAuthor");
        System.out.println("$$$$$$$$$$$$$authorIdX: " + authorIdX);
        System.out.println("$$$$$$$$$$$$$firstName: " + firstName);
        System.out.println("$$$$$$$$$$$$$lastName: " + lastName);
        System.out.println("$$$$$$$$$$$$$birthday,: " + birthday);
        System.out.println("$$$$$$$$$$$$$deathday: " + deathday);
        System.out.println("^^^^^^^^" + birthday.charAt(4));
        savedAuthorId = authorIdX;
        if(authorIdX == 0){  //If a new author was entered, enter into dB.
            Author newAth = new Author(birthday, deathday, firstName, lastName);
            authorRepository.save(newAth);
            savedAuthorId = newAth.getAuthorId();
        }
        System.out.println("$$$$$$$$$$$$$savedAuthorId : " + savedAuthorId);

        //model.addAttribute("authorId", authorId);
        return "redirect:genre?newBook=true";  //calls GetMapping genre
    }



    @PostMapping("test")  //  responds to a browser post request to /library/addBook.
    public String test(@RequestParam(required = false) Model model) {
        System.out.println("InPostMappingATest");

        return "redirect:book";  //calls GetMapping book
    }

    @PostMapping("debug")   //not used, method of direct SQL changes
    public String processDebugForm(@RequestParam(required = false) int patronId) {
        System.out.println("InPostMappingPatron");
        savedPatronId = patronId;
        System.out.println("####savedPatronId: " + savedPatronId);

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        //Above are session init statements

        Query query1 = session.createSQLQuery(
                "UPDATE patron SET loan_id = :loanIdX" + " WHERE patron_id = :patronIdX");
        query1.setParameter("loanIdX", 999);
        query1.setParameter("patronIdX", 6);
        int result1 = query1.executeUpdate();
        System.out.println("result1: " + result1);

        Patron fred = new Patron("Peter", "Bessey",
                "777 Old Farm,Greenville, IL 62246", "618-698-4000");
        System.out.println("&&&&PatronID: " + fred.getFirstName());

 /*       Query query2 = session.createSQLQuery(
                "INSERT INTO patron (first_name, last_name, Loan_id)" +
                        "VALUES (:firstNameX, :lastNameX, :LoanIdX)");
        query2.setParameter("firstNameX", "Peter");
        query2.setParameter("lastNameX", "Bessey");
        query2.setParameter("LoanIdX", 22);
        int result2 = query2.executeUpdate();
        System.out.println("result2: " + result2);
        //savedLoanId = 77;
        //Patron patronResult = patronRepository.findByPatronId(savedPatronId);
        //System.out.println("$$$$LOanResult: " + patronResult);

  */
/*
        Query query3 = session.createSQLQuery(
                "UPDATE patron SET loan_id = :loanID" + " WHERE patronId = :patronID");
        //"SELECT loan_id FROM loan WHERE loan.patronId = :patronId");
        query3.setParameter("patronID", savedPatronId);
        query3.setParameter("loanID", savedLoanId);

        int result3 = query3.executeUpdate();
        System.out.println("result3: " + result3);
*/
        // Below are session activation and closing statements
        session.getTransaction().commit();
        session.clear();
        session.close();

        return "redirect:patron";
    }
}





/*
    @GetMapping("dbTest")  //  responds to a browser Get request to /library/dbTest.
    public String dbTest(Model model) {
        System.out.println("InGetdbTest");  //debug

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();

        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "UPDATE Book SET available = :bookAvail" + " WHERE book_id = :bookId");
        query.setParameter("bookAvail", false);
        query.setParameter("bookId", 6);
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();

        return "library/book";  //calls the book template
    }

    @GetMapping("loan")  //  responds to a browser Get request to /library/loan.
    public String displayAllLoans(Model model) {
        System.out.println("InGetMappingLoan");  //debug
        //savedBookId = 4;
        //savedPatronId = 4;
        model.addAttribute("title", "All Loans");
        model.addAttribute("loans", loanRepository.findAll());

        if (savedBookId == 0 || savedPatronId == 0) {
            return "redirect:book";  //calls the GetMapping book controller method
        } else {

            Optional bookResult = bookRepository.findById(savedBookId);
            Optional patronResult = patronRepository.findById(savedPatronId);
            if (bookResult.isEmpty() || patronResult.isEmpty()) {
                return "redirect:book";  //calls the GetMapping book controller method
            } else {
                theBook = bookResult.get();
                thePatron = patronResult.get();
                System.out.println("thePatron: " + thePatron);
                model.addAttribute("heading", "EXECUTE LOAN");
                model.addAttribute("theBook", theBook);
                model.addAttribute("thePatron", thePatron);
                return "library/loan";  //calls the loan template
            }
        }

    }


        @PostMapping("patron")
        public String processPatronSelectForm(@RequestParam(required = false) int patronId) {
  */  /*
    @PostMapping("loan")
    public String processLoanForm() {
        System.out.println("InPostMappingLoan");
        //System.out.println("####theBook: " + theBook);
        //System.out.println("####thePatron: " + thePatron);

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        //Above are session init statements

        Query query1 = session.createSQLQuery(
                "UPDATE book SET available = :bookAvail" + " WHERE book_id = :bookId");
        query1.setParameter("bookAvail", false);
        query1.setParameter("bookId", savedBookId);
        int result1 = query1.executeUpdate();
        System.out.println("result1: " + result1);

        Query query2 = session.createSQLQuery(
                "INSERT INTO loan (date_out, patronId, book_id)" +
                        "VALUES (:todaysDate, :patronID, :bookID)");
        query2.setParameter("todaysDate", LocalDateTime.now());
        query2.setParameter("patronID", savedPatronId);
        query2.setParameter("bookID", savedBookId);
        int result2 = query2.executeUpdate();
        System.out.println("result2: " + result2);
        savedLoanId = 77;
        Loan loanResult = loanRepository.findByPatronId(savedPatronId);
        System.out.println("$$$$LOanResult: " + loanResult);

        Query query3 = session.createSQLQuery(
                "UPDATE patron SET loan_id = :loanID" + " WHERE patronId = :patronID");
        //"SELECT loan_id FROM loan WHERE loan.patronId = :patronId");
        query3.setParameter("patronID", savedPatronId);
        query3.setParameter("loanID", savedLoanId);

        int result3 = query3.executeUpdate();
        System.out.println("result3: " + result3);

        // Below are session activation and closing statements
        session.getTransaction().commit();
        session.clear();
        session.close();

        //return "library/book";  //calls the book template
        return "redirect:book";
    }
}

/*
UPDATE book
SET available = 0
WHERE (title LIKE @title);

INSERT INTO loan (date_out, patron_id, book_id)
	VALUES(@todays_date, @patron_id, @book_id
);

UPDATE patron
SET loan_id = (SELECT loan_id FROM loan WHERE(loan.patron_id = @patron_id))
 WHERE patron_id = @patron_id;
@@@@@@@@@@@@@@@
if (categoryId == null) {
   model.addAttribute("title", "All Events");
   model.addAttribute("events", eventRepository.findAll());
} else {
   Optional<EventCategory> result = eventCategoryRepository.findById(categoryId);
   if (result.isEmpty()) {
         model.addAttribute("title", "Invalid Category ID: " + categoryId);
   } else {
         EventCategory category = result.get();
         model.addAttribute("title", "Events in category: " + category.getName());
         model.addAttribute("events", category.getEvents());
   }
}
&&&&&&&&&&&&&&&&&&&&&&
    @GetMapping("create")
    public String displayCreateEventForm(Model model) {
        model.addAttribute("title", "Create Event");
        model.addAttribute(new Event());
        model.addAttribute("types", EventType.values());
        return "events/create";
    }

    @PostMapping("create")
    public String processCreateEventForm(@ModelAttribute @Valid Event newEvent,
                                         Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("title", "Create Event");
            return "events/create";
        }

        //EventData.add(newEvent);  // for persistence changed to line below.
        eventRepository.save(newEvent);
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteEventForm(Model model) {
        model.addAttribute("title", "Delete Events");
        //model.addAttribute("events", EventData.getAll());  //for persistence changed to line below.
        model.addAttribute("events", eventRepository.findAll());
        return "events/delete";
    }

    @PostMapping("bookId")
    public String processDeleteEventsForm(@RequestParam(required = false) int[] bookId) {

        if (eventIds != null) {
            for (int id : eventIds) {
                //EventData.remove(id);  // for persistence changed to line below.
                eventRepository.deleteById(id);
            }
        }

        return "redirect:";
    }
*/


