package practice.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.library.models.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {

    List<Book> findBookByTitleContainingIgnoreCase (String contain);
}
