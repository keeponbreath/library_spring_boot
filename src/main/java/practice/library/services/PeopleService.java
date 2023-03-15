package practice.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.library.models.Book;
import practice.library.models.Person;
import practice.library.repositories.PeopleRepository;
import practice.library.security.PersonDetails;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Person> index() {
        return peopleRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Person> index(boolean sorted) {
        if (sorted) {
            return peopleRepository.findAll(Sort.by("dateOfBirth"));
        }
        else {
            return peopleRepository.findAll();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Person> index(Integer page, Integer peoplePerPage, boolean sorted) {
        if(sorted) {
            return peopleRepository.findAll(PageRequest.of(page, peoplePerPage,
                    Sort.by("dateOfBirth"))).getContent();
        } else return peopleRepository.findAll(PageRequest.of(page, peoplePerPage)).getContent();
    }

    @PreAuthorize("(principal.getPerson().getId == #id) or (hasRole('ROLE_ADMIN'))")
    public Person show(long id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Optional<Person> loadUserByUsername(String username) {
        return peopleRepository.findPersonByUsername(username);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(long id, Person updatedPerson) {
        updatedPerson.setId(id);
        updatedPerson.setRole("ROLE_USER");
        updatedPerson.setPassword(passwordEncoder.encode(updatedPerson.getPassword()));
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(long id) {
        peopleRepository.deleteById(id);
    }

    @PreAuthorize("principal.getPerson().getId == #id")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Person> search(String contain) {
        return peopleRepository.findPersonByNameContainingIgnoreCase(contain);
    }

    public PersonDetails getPersonDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) auth.getPrincipal();
        return personDetails;
    }
}