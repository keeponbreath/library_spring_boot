package practice.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import practice.library.models.Person;
import practice.library.services.BookService;
import practice.library.services.PeopleService;



@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    private final BookService bookService;

    @Autowired
    public PeopleController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer pageNumber,
                        @RequestParam(value = "people_per_page", required = false) Integer peoplePerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sorted) {
        roleChecker(model);

        if(pageNumber == null || peoplePerPage == null) {
            model.addAttribute("people", peopleService.index(sorted));
        } else {
            model.addAttribute("people", peopleService.index(pageNumber, peoplePerPage, sorted));
        }
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        roleChecker(model);

        model.addAttribute("person", peopleService.show(id));
        model.addAttribute("books", peopleService.getBooks(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

    @GetMapping("/search")
    public String findPerson(@ModelAttribute("pointer") String pointer,
                             Model model) {
        model.addAttribute("pointer", pointer);
        return "people/search";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("pointer") String pointer,
                         Model model) {
        model.addAttribute("founded", peopleService.search(pointer));
        return "people/search";
    }

    public void roleChecker(Model model) {
        if(peopleService.getPersonDetails().getPerson().getRole().equals("ROLE_ADMIN"))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isUser", true);
    }
}