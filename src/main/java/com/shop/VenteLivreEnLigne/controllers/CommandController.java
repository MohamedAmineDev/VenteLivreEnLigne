package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.*;
import com.shop.VenteLivreEnLigne.repositories.AppUserRepository;
import com.shop.VenteLivreEnLigne.repositories.BookRepository;
import com.shop.VenteLivreEnLigne.repositories.CommandRepository;
import com.shop.VenteLivreEnLigne.repositories.CommandedBookRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CommandController {
    private final AppUserRepository appUserRepository;
    private final BookRepository bookRepository;
    private final CommandRepository commandRepository;
    private final CommandedBookRepository commandedBookRepository;

    @RequestMapping("/client/put_book")
    public String addInTheBasket(Model model, @RequestParam(name = "id") UUID id, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Book book = bookRepository.findById(id).orElse(null);
        model.addAttribute("item", new BasketItem(book.getId(), 1l, book.getTitle(), book.getImageLink(), book.getPrice()));
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("bookMax", bookRepository.findAll().size());
        model.addAttribute("error", false);
        model.addAttribute("phrase", "");
        return "add_book_basket.html";
    }

    @RequestMapping("/client/put_the_book")
    public String saveBookInTheBasket(Model model, @Valid BasketItem basketItem, BindingResult bindingResult, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "add_book_basket.html";
        }
        Book book = bookRepository.findById(basketItem.getBookId()).orElse(null);
        if (book.getQuantity() < basketItem.getQuantity()) {
            model.addAttribute("item", new BasketItem(book.getId(), 1l, book.getTitle(), book.getImageLink(), book.getPrice()));
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            model.addAttribute("bookMax", book.getQuantity());
            model.addAttribute("error", true);
            model.addAttribute("phrase", "Only " + book.getQuantity() + " left exemples !");
            return "add_book_basket.html";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser u = appUserRepository.findByUsername(authentication.getName());
        if (u != null) {
            System.out.println("********************************************************");
            //Shopping.shoppingBasketHashMap.get(u.getId()).addBook(shoppingItem);
            Basket.addItemToTheBasket(u.getId(), basketItem);
            System.out.println("********************************************************");
        }
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    @RequestMapping("/client/basket")
    public String getBasket(Model model, @RequestParam(name = "info", defaultValue = "false") boolean info) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser u = appUserRepository.findByUsername(authentication.getName());
        List<BasketItem> items = Basket.storages.get(u.getId());
        model.addAttribute("items", items);
        return "basket.html";
    }

    @RequestMapping("/client/delete_basket_book")
    public String deleteBookFromBasket(@RequestParam(name = "book_id") UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser u = appUserRepository.findByUsername(authentication.getName());
        Basket.deleteBook(u.getId(), id);
        return "redirect:/client/basket";
    }

    @RequestMapping("/client/pass_command")
    public String buyBook() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser u = appUserRepository.findByUsername(authentication.getName());
        List<BasketItem> basketItems = Basket.storages.get(u.getId());
        Command command = new Command();
        command.setCommandedAt(LocalDateTime.now());
        command = commandRepository.saveAndFlush(command);
        List<Book> books = new ArrayList<>();
        List<CommandedBook> commandedBooks = new ArrayList<>();
        for (BasketItem b : basketItems
        ) {
            Book book = bookRepository.findById(b.getBookId()).orElse(null);
            book.setQuantity(book.getQuantity() - b.getQuantity());
            books.add(book);
            commandedBooks.add(new CommandedBook(null, b.getQuantity(), book, command));
        }
        commandedBookRepository.saveAllAndFlush(commandedBooks);
        bookRepository.saveAllAndFlush(books);
        return "";
    }
}
