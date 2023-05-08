package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.Book;
import com.shop.VenteLivreEnLigne.models.Category;
import com.shop.VenteLivreEnLigne.models.Writer;
import com.shop.VenteLivreEnLigne.repositories.BookRepository;
import com.shop.VenteLivreEnLigne.repositories.WriterRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WriterController {
    private final WriterRepository writerRepository;
    private final BookRepository bookRepository;

    @RequestMapping("/writers")
    public String writers(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Writer> writerPage = writerRepository.findByNameContains(keyword, PageRequest.of(page, size));
        model.addAttribute("writers", writerPage.getContent());
        model.addAttribute("pages", new int[writerPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "writers.html";
    }

    @RequestMapping("/delete_writer")
    public String deleteWriter(@RequestParam(name = "id") UUID id, @RequestParam(name = "page") int page, @RequestParam(name = "keyword") String keyword) {
        try {
            List<Book> books = bookRepository.findByWriterById(id);
            bookRepository.deleteAll(books);
            writerRepository.deleteById(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            return "redirect:/writers?page=" + page + "&keyword=" + keyword;
        }
    }

    @RequestMapping("/add_writer")
    public String addWriter(Model model) {
        model.addAttribute("writer", new Writer());
        return "add_writer.html";
    }

    @RequestMapping("/save_writer")
    public String saveWriter(Model model, @Valid Writer writer, BindingResult bindingResult, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            return "add_writer.html";
        }
        writerRepository.saveAndFlush(writer);
        return "redirect:/writers?page=" + page + "&keyword=" + keyword;
    }

    @RequestMapping("/edit_writer")
    public String editWriter(Model model, @RequestParam(name = "id") UUID id, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        try {
            Writer writer = writerRepository.findById(id).orElseThrow(() -> new RuntimeException("Writer not found !"));
            model.addAttribute("writer", writer);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            return "edit_writer.html";
        } catch (Exception exception) {
            exception.printStackTrace();
            return "redirect:/writers?page=" + page + "&keyword=" + keyword;
        }
    }
}
