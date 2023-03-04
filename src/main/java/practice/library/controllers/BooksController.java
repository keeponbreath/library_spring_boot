package practice.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import practice.library.models.Book;
import practice.library.models.Person;
import practice.library.services.BookService;
import practice.library.services.PeopleService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer pageNumber,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sorted) {
        if(pageNumber == null || booksPerPage == null) {
            model.addAttribute("books", bookService.index(sorted));
        } else {
            model.addAttribute("books", bookService.index(pageNumber, booksPerPage, sorted));
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.show(id));
        if (bookService.getOwner(id) != null) {
            model.addAttribute("owner", bookService.getOwner(id));
        } else {
            model.addAttribute("people", peopleService.index());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "books/new";

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors())
            return "books/edit";

        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String findBook(@ModelAttribute("pointer") String pointer,
                           Model model) {
        model.addAttribute("pointer", pointer);
        return "books/search";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("pointer") String pointer,
                         Model model) {
        model.addAttribute("founded", bookService.search(pointer));
        return "books/search";
    }

    @PatchMapping("{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        bookService.freeBook(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/take")
    public String takeBook(@PathVariable("id") int id,
                           @ModelAttribute("person") Person person) {
        bookService.takeBook(id, person);
        return "redirect:/books/" + id;
    }
}