package practice.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import practice.library.models.Person;
import practice.library.repositories.PeopleRepository;
import practice.library.security.PersonDetails;

import java.util.Optional;

@Service
public class PeopleDetailService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleDetailService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findPersonByUsername(username);
        if(person.isEmpty())
            throw new UsernameNotFoundException("Пользователь с таким именем не найден!");

        return new PersonDetails(person.get());
    }
}
