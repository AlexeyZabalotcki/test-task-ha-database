package com.zabalotckialexey.testtaskh2db.controller;

import com.zabalotckialexey.testtaskh2db.model.Book;
import com.zabalotckialexey.testtaskh2db.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/all")
    public List<Book> findAll() {
        return bookService.getAllBooks();
    }

    @PostMapping("/add")
    public ResponseEntity<Book> add(@RequestBody Book book) {
        if (book.getId() != null && book.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (book.getAuthor() == null || book.getAuthor().trim().length() == 0) {
            return new ResponseEntity("missed param: author", HttpStatus.NOT_ACCEPTABLE);
        }
        if (book.getTitle() == null || book.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        if (book.getPublisher() == null || book.getPublisher().trim().length() == 0) {
            return new ResponseEntity("missed param: publisher", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(bookService.add(book));
    }

    @PutMapping("/update")
    public ResponseEntity<Book> update(@RequestBody Book book) {
        if (book.getId() == null || book.getId() == 0) {
            return new ResponseEntity("Error: id MUST be fill", HttpStatus.NOT_ACCEPTABLE);
        }
        if (book.getAuthor() == null || book.getAuthor().trim().length() == 0) {
            return new ResponseEntity("missed param: you MUST fill author form", HttpStatus.NOT_ACCEPTABLE);
        }
        if (book.getTitle() == null || book.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: you MUST fill title form", HttpStatus.NOT_ACCEPTABLE);
        }

        bookService.update(book);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        Book book = null;

        try {
            book = bookService.findBookById(id);
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            return new ResponseEntity("id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Book> deleteById(@PathVariable Long id) {
        try {
            bookService.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Book>> search(@RequestParam("title") String title) {
        return ResponseEntity.ok(bookService.findBookByTitle(title));
    }

    @PostMapping("/search-date")
    public ResponseEntity<List<Book>> searchDate(@RequestParam("date") String date) {
        return ResponseEntity.ok(bookService.findBookByDate(date));
    }
}
