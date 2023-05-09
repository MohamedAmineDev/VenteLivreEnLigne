package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.Book;
import com.shop.VenteLivreEnLigne.models.Category;
import com.shop.VenteLivreEnLigne.models.Writer;
import com.shop.VenteLivreEnLigne.repositories.BookRepository;
import com.shop.VenteLivreEnLigne.repositories.CategoryRepository;
import com.shop.VenteLivreEnLigne.repositories.WriterRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final WriterRepository writerRepository;

    @RequestMapping("/user/index")
    public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Book> bookPage = bookRepository.findByTitleContains(keyword, PageRequest.of(page, size));
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("pages", new int[bookPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "index.html";
    }

    @RequestMapping("/")
    public String redirectToIndex(Model model) {
        return "redirect:/user/index";
    }

    @RequestMapping("/admin/delete_book")
    public String deleteBook(@RequestParam(name = "id") UUID id, @RequestParam(name = "page") int page, @RequestParam(name = "keyword") String keyword) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
        }
    }

    @RequestMapping("/admin/add_book")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("writers", writerRepository.findAll());
        return "add_book.html";
    }

    @PostMapping("/admin/save_book")
    public String saveBook(Model model, @Valid Book book, BindingResult bindingResult, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("writers", writerRepository.findAll());
            return "add_book.html";
        }
        book.setCategory(new Category(book.getCategoryId()));
        book.setWriter(new Writer(book.getWriterId()));
        bookRepository.save(book);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    @RequestMapping("/admin/edit_book")
    public String editBook(Model model, @RequestParam(name = "id") UUID id, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Book book = bookRepository.findById(id).orElse(null);
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("writers", writerRepository.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "edit_book.html";
    }
}
