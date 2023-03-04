package practice.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.library.models.Book;
import practice.library.models.Person;
import practice.library.repositories.PeopleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index() {
        return peopleRepository.findAll();
    }

    public List<Person> index(boolean sorted) {
        if (sorted) {
            return peopleRepository.findAll(Sort.by("dateOfBirth"));
        }
        else {
            return peopleRepository.findAll();
        }
    }

    public List<Person> index(Integer page, Integer peoplePerPage, boolean sorted) {
        if(sorted) {
            return peopleRepository.findAll(PageRequest.of(page, peoplePerPage,
                    Sort.by("dateOfBirth"))).getContent();
        } else return peopleRepository.findAll(PageRequest.of(page, peoplePerPage)).getContent();
    }

    public Person show(long id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(long id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(long id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooks(long id) {
        Optional<Person> personOptional = peopleRepository.findById(id);

        if (personOptional.isPresent()) {
            Hibernate.initialize(personOptional.get().getBooks());
            for (Book book : personOptional.get().getBooks()) {
                final long convert = 24*60*60*1000;
                if ((new Date().getTime() - book.getTakenAt().getTime())/convert >= 10) {
                    book.setOverdue(true);
                } else {
                    book.setOverdue(false);
                }
            }
            return personOptional.get().getBooks();
        }
        else
            return Collections.emptyList();
    }

    public List<Person> search(String contain) {
        return peopleRepository.findPersonByNameContainingIgnoreCase(contain);
    }
}