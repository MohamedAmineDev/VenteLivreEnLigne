package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.Book;
import com.shop.VenteLivreEnLigne.models.Genre;
import com.shop.VenteLivreEnLigne.repositories.BookRepository;
import com.shop.VenteLivreEnLigne.repositories.GenreRepository;
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
public class GenreController {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @RequestMapping("/user/genres")
    public String categories(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Genre> categoryPage = genreRepository.findByLabelContains(keyword, PageRequest.of(page, size));
        model.addAttribute("genres", categoryPage.getContent());
        model.addAttribute("pages", new int[categoryPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "genres.html";
    }

    @RequestMapping("/admin/delete_genre")
    public String deleteCategory(@RequestParam(name = "id") UUID id, @RequestParam(name = "page") int page, @RequestParam(name = "keyword") String keyword) {
        try {
            List<Book> books = bookRepository.findByGenreById(id);
            bookRepository.deleteAll(books);
            bookRepository.flush();
            genreRepository.deleteById(id);
            genreRepository.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            return "redirect:/user/genres?page=" + page + "&keyword=" + keyword;
        }
    }

    @RequestMapping("/admin/add_genre")
    public String addCategory(Model model) {
        model.addAttribute("genre", new Genre());
        return "add_genre.html";
    }

    @PostMapping("/admin/save_genre")
    public String saveCategory(Model model, @Valid Genre genre, BindingResult bindingResult, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            return "add_genre.html";
        }
        genreRepository.saveAndFlush(genre);
        return "redirect:/user/genres?page=" + page + "&keyword=" + keyword;
    }

    @RequestMapping("/admin/edit_genre")
    public String editCategory(Model model, @RequestParam(name = "id") UUID id, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        try {
            Genre genre = genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found !"));
            model.addAttribute("genre", genre);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            return "edit_genre.html";
        } catch (Exception exception) {
            exception.printStackTrace();
            return "redirect:/user/genres?page=" + page + "&keyword=" + keyword;
        }
    }
}
