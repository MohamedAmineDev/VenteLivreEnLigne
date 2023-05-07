package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.Book;
import com.shop.VenteLivreEnLigne.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;

    @RequestMapping("/index")
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
        return "redirect:index";
    }

    @RequestMapping("/delete_book")
    public String deleteBook(@RequestParam(name = "id") UUID id, @RequestParam(name = "page") int page, @RequestParam(name = "keyword") String keyword) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            return "redirect:/index?page=" + page + "&keyword=" + keyword;
        }
    }
}
