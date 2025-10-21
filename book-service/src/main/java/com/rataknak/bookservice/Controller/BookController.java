package com.rataknak.bookservice.Controller;

import com.rataknak.bookservice.Entity.Book;
import com.rataknak.bookservice.Repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @PostMapping
    public Book createBook(@RequestParam Long userId, @RequestBody Book book) {
        book.setUserId(userId);
        return bookRepository.save(book);
    }

    @GetMapping("/user/{userId}")
    public List<Book> getBooksByUserId(@PathVariable Long userId) {
        return bookRepository.findAllByUserId(userId);
    }
}