package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.*;
import com.shop.VenteLivreEnLigne.repositories.AppUserRepository;
import com.shop.VenteLivreEnLigne.repositories.BookRepository;
import com.shop.VenteLivreEnLigne.repositories.CommandRepository;
import com.shop.VenteLivreEnLigne.repositories.CommandedBookRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public String getBasket(Model model, @RequestParam(name = "info", defaultValue = "false") boolean info, @RequestParam(name = "error", defaultValue = "false") boolean error, @RequestParam(name = "errorMessage", defaultValue = "") String errorMessage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser u = appUserRepository.findByUsername(authentication.getName());
        List<BasketItem> items = Basket.storages.get(u.getId());
        model.addAttribute("items", items);
        model.addAttribute("info", info);
        model.addAttribute("error", error);
        model.addAttribute("errorMessage", errorMessage);
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
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUser u = appUserRepository.findByUsername(authentication.getName());
            List<BasketItem> basketItems = Basket.storages.get(u.getId());
            Command command = new Command();
            List<Book> books = new ArrayList<>();
            List<CommandedBook> commandedBooks = new ArrayList<>();
            double totalPrice = 0;
            for (BasketItem b : basketItems
            ) {
                Book book = bookRepository.findById(b.getBookId()).orElseThrow(() -> new Exception("Product " + b.getBookTitle() + " not found !"));
                if (book.getQuantity() < b.getQuantity()) {
                    throw new Exception("The quantity of " + b.getBookTitle() + " is not enough , we currently have  " + book.getQuantity());
                }
                book.setQuantity(book.getQuantity() - b.getQuantity());
                books.add(book);
                commandedBooks.add(new CommandedBook(null, b.getQuantity(), book, command));
                totalPrice += b.getPrice() * b.getQuantity();
            }
            command.setCommandedAt(LocalDateTime.now());
            command.setStatus(CommandStatus.NotDelivered);
            command.setUser(u);
            command.setTotalPrice(totalPrice);
            command = commandRepository.saveAndFlush(command);
            commandedBookRepository.saveAllAndFlush(commandedBooks);
            bookRepository.saveAllAndFlush(books);
            Basket.storages.get(u.getId()).clear();
            return "redirect:/client/basket?info=" + true + "&error=" + false + "&errorMessage=";
        } catch (Exception exception) {
            exception.printStackTrace();
            return "redirect:/client/basket?info=" + false + "&error=" + true + "&errorMessage=" + exception.getMessage();
        }
    }

    @RequestMapping("/client/all_my_not_delivered_commands")
    public String getAllNotDeliveredCommandsForAUser(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser u = appUserRepository.findByUsername(authentication.getName());
        Page<Command> commandPage = commandRepository.findAllCommandsByStatus(u.getId(), CommandStatus.NotDelivered, PageRequest.of(page, size));
        model.addAttribute("commands", commandPage.getContent());
        model.addAttribute("pages", new int[commandPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "my-all-not-delivered-commands.html";
    }

    @RequestMapping("/client/all_my_delivered_commands")
    public String getAllDeliveredCommandsForAUser(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser u = appUserRepository.findByUsername(authentication.getName());
        Page<Command> commandPage = commandRepository.findAllCommandsByStatus(u.getId(), CommandStatus.Delivered, PageRequest.of(page, size));
        model.addAttribute("commands", commandPage.getContent());
        model.addAttribute("pages", new int[commandPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "my-all-delivered-commands.html";
    }

    @RequestMapping("/client/command/see_details")
    public String getCommandDetails(Model model, @RequestParam(name = "command_id", defaultValue = "") UUID commandId) {
        List<CommandedBook> commandedBooks = commandedBookRepository.findByCommandById(commandId);
        Command command = commandRepository.findById(commandId).orElse(null);
        model.addAttribute("details", commandedBooks);
        model.addAttribute("totalPrice", command.getTotalPrice());
        return "command-details.html";
    }

    @RequestMapping("/admin/all_not_delivered_commands")
    public String getAllNotDeliveredCommands(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        Page<Command> commandPage = commandRepository.findAllCommandsByStatus(CommandStatus.NotDelivered, PageRequest.of(page, size));
        model.addAttribute("commands", commandPage.getContent());
        model.addAttribute("pages", new int[commandPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "all-not-delivered-commands.html";
    }

    @RequestMapping("/admin/all_delivered_commands")
    public String getAllDeliveredCommands(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        Page<Command> commandPage = commandRepository.findAllCommandsByStatus(CommandStatus.Delivered, PageRequest.of(page, size));
        model.addAttribute("commands", commandPage.getContent());
        model.addAttribute("pages", new int[commandPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "all-delivered-commands.html";
    }

    @RequestMapping("/admin/command/see_details")
    public String getCommandDetailsForAnAdmin(Model model, @RequestParam(name = "command_id", defaultValue = "") UUID commandId) {
        List<CommandedBook> commandedBooks = commandedBookRepository.findByCommandById(commandId);
        Command command = commandRepository.findById(commandId).orElse(null);
        model.addAttribute("details", commandedBooks);
        model.addAttribute("totalPrice", command.getTotalPrice());
        return "command-details.html";
    }

    @RequestMapping("/admin/command/deliver")
    public String deliverACommand(@RequestParam(name = "command_id", defaultValue = "") UUID commandId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        Command command = commandRepository.findById(commandId).orElse(null);
        command.setStatus(CommandStatus.Delivered);
        commandRepository.save(command);
        return "redirect:/admin/all_not_delivered_commands?page=" + page + "&size=" + size;
    }

    @RequestMapping("/admin/command/did_not_deliver")
    public String didNotDeliverACommand(@RequestParam(name = "command_id", defaultValue = "") UUID commandId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        Command command = commandRepository.findById(commandId).orElse(null);
        command.setStatus(CommandStatus.NotDelivered);
        commandRepository.save(command);
        return "redirect:/admin/all_delivered_commands?page=" + page + "&size=" + size;
    }
}
