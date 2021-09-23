package com.sertaperdana.libraryApp.data;


import com.sertaperdana.libraryApp.models.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository  //Signifies to SpringBoot that this class will store objects in the DB.
public interface GenreRepository extends CrudRepository<Genre, Integer> {
    // This interface class was added / is needed for persistence.
    // <Patron for type of thing we are storing.
    // Integer> for data type of the primary key for that class.
    Genre findByGenreId(int genreId);
}
