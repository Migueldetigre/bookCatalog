package com.alurachallenges.literalura.bookfinder;

import com.alurachallenges.literalura.bookfinder.bookfinderRepository.BuchRepository;
import com.alurachallenges.literalura.bookfinder.bookfinderRepository.SchöpferRepository;
import com.alurachallenges.literalura.bookfinder.bookfinderMain.BookFinderApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BookFinderApplication implements CommandLineRunner{
    @Autowired
    private SchöpferRepository schöpferRepository;

    @Autowired
    private BuchRepository buchRepository;

    public BookFinderApplication(SchöpferRepository schöpferRepository, BuchRepository buchRepository) {

    }

    public static void main(String[] args) {
        SpringApplication.run(BookFinderApplication.class, args);
    }

    @Override
    public void run(String... args){
        BookFinderApp bookFinderApp = new BookFinderApp(schöpferRepository, buchRepository);
        bookFinderApp.queryExample(); // Llamamos al método desde la instancia de BookFinderApp
    }
}

