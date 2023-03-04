package practice.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.library.models.Person;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {

    List<Person> findPersonByNameContainingIgnoreCase (String contain);
}
