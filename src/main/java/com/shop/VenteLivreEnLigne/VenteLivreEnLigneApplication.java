package com.shop.VenteLivreEnLigne;

import com.shop.VenteLivreEnLigne.models.AppUser;
import com.shop.VenteLivreEnLigne.models.Book;
import com.shop.VenteLivreEnLigne.models.Genre;
import com.shop.VenteLivreEnLigne.models.Writer;
import com.shop.VenteLivreEnLigne.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class VenteLivreEnLigneApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenteLivreEnLigneApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(BookRepository bookRepository, WriterRepository writerRepository, GenreRepository genreRepository) {
        return args -> {
            List<Writer> writers = new ArrayList<>();
//            writers.add(new Writer(null, "Author 1", "Author 1", "https://cdn.britannica.com/88/213788-050-061733DC/English-novelist-Agatha-Christie-circa-1925.jpg"));
//            writers.add(new Writer(null, "Author 2", "Author 2", "https://cdn.britannica.com/88/213788-050-061733DC/English-novelist-Agatha-Christie-circa-1925.jpg"));
//            writers.add(new Writer(null, "Author 3", "Author 3", "https://cdn.britannica.com/88/213788-050-061733DC/English-novelist-Agatha-Christie-circa-1925.jpg"));
//            writers.add(new Writer(null, "Author 4", "Author 4", "https://cdn.britannica.com/88/213788-050-061733DC/English-novelist-Agatha-Christie-circa-1925.jpg"));
//            writers.add(new Writer(null, "Author 5", "Author 5", "https://cdn.britannica.com/88/213788-050-061733DC/English-novelist-Agatha-Christie-circa-1925.jpg"));
//            writers.add(new Writer(null, "Author 6", "Author 6", "https://cdn.britannica.com/88/213788-050-061733DC/English-novelist-Agatha-Christie-circa-1925.jpg"));
//            writerRepository.saveAllAndFlush(writers);
            List<Genre> categories = new ArrayList<>();
//            categories.add(new Category(null, "category 1", "image", null));
//            categories.add(new Category(null, "category 2", "image", null));
//            categories.add(new Category(null, "category 3", "image", null));
//            categories.add(new Category(null, "category 4", "image", null));
//            categories.add(new Category(null, "category 5", "image", null));
//            categories.add(new Category(null, "category 6", "image", null));
//            categoryRepository.saveAllAndFlush(categories);
            writers = writerRepository.findAll();
            categories = genreRepository.findAll();
            List<Book> books = new ArrayList<>();
            for (int i = 0; i < writers.size(); i++) {
                books.add(new Book(null, "Book " + (i + 1), "https://d1csarkz8obe9u.cloudfront.net/posterpreviews/roman-history-book-cover-design-template-48914ad1e972113cd5e8508a05ad2904_screen.jpg?ts=1637014074", 20l, new Date(), 85.5, categories.get(i), writers.get(i)));
            }
            bookRepository.saveAllAndFlush(books);
        };
    }

    //@Bean
    CommandLineRunner commandLineRunnerUserDetails(AccountService accountService, AppUserRepository appUserRepository) {
        return args -> {
            /*accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");
            accountService.addNewUser("user1", "123", "user1@gmail.com", "123");
            accountService.addNewUser("user2", "123", "user2@gmail.com", "123");
            accountService.addNewUser("user3", "123", "user3@gmail.com", "123");
            accountService.addNewUser("admin", "123", "admin@gmail.com", "123");
            accountService.addRoleToUser("user1", "USER");
            accountService.addRoleToUser("user2", "USER");
            accountService.addRoleToUser("user3", "USER");
            accountService.addRoleToUser("admin", "USER");
            accountService.addRoleToUser("admin", "ADMIN");
             */
            //accountService.addNewRole("CLIENT");
            AppUser appUser = appUserRepository.findByUsername("admin@gmail.com");
            if (appUser == null) {
                accountService.addNewRole("USER");
                accountService.addNewRole("ADMIN");
                accountService.addNewRole("CLIENT");
                accountService.addNewUser("admin", "mmmmmm", "admin@gmail.com", "mmmmmm");
                accountService.addRoleToUser("admin", "USER");
                accountService.addRoleToUser("admin", "ADMIN");
            }
        };
    }
}
