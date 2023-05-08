package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.Book;
import com.shop.VenteLivreEnLigne.models.Category;
import com.shop.VenteLivreEnLigne.repositories.BookRepository;
import com.shop.VenteLivreEnLigne.repositories.CategoryRepository;
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
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @RequestMapping("/categories")
    public String categories(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Category> categoryPage = categoryRepository.findByLabelContains(keyword, PageRequest.of(page, size));
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("pages", new int[categoryPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "categories.html";
    }

    @RequestMapping("/delete_category")
    public String deleteCategory(@RequestParam(name = "id") UUID id, @RequestParam(name = "page") int page, @RequestParam(name = "keyword") String keyword) {
        try {
            List<Book> books = bookRepository.findByCategoryById(id);
            bookRepository.deleteAll(books);
            bookRepository.flush();
            categoryRepository.deleteById(id);
            categoryRepository.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            return "redirect:/categories?page=" + page + "&keyword=" + keyword;
        }
    }

    @RequestMapping("/add_category")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "add_category.html";
    }

    @PostMapping("/save_category")
    public String saveCategory(Model model, @Valid Category category, BindingResult bindingResult, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            return "add_category.html";
        }
        categoryRepository.saveAndFlush(category);
        return "redirect:/categories?page=" + page + "&keyword=" + keyword;
    }

    @RequestMapping("/edit_category")
    public String editCategory(Model model, @RequestParam(name = "id") UUID id, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found !"));
            model.addAttribute("category", category);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            return "edit_category.html";
        } catch (Exception exception) {
            exception.printStackTrace();
            return "redirect:/categories?page=" + page + "&keyword=" + keyword;
        }
    }
}
