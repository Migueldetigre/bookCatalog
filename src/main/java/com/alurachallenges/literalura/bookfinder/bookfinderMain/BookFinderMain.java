/*package com.alurachallenges.literalura.bookfinder.bookfinderMain;

import com.alurachallenges.literalura.bookfinder.model.*;
import com.alurachallenges.literalura.bookfinder.bookfinderRepository.BuchRepository;
import com.alurachallenges.literalura.bookfinder.bookfinderRepository.SchöpferRepository;
import com.alurachallenges.literalura.bookfinder.service.BookFinderConsume;

import java.util.Scanner;
import java.util.List;

public class BookFinderApplication{
    private static final String adresseVerbindung = "https://gutendex.com/books/";
    //private UnwandelnDaten konverter = new
    private BookFinderConsume query = new BookFinderConsume();
    private int UiOptione = -1;
    private SchöpferRepository schöpferRepository;
    private BuchRepository buchRepository;
    List<Autor> autoren;
    List<Buch> bücher;
    Scanner tastaturEingabe = new Scanner(System.in);

    public BookFinderApplication(SchöpferRepository schöpferRepository, BuchRepository buchRepository) {
        this.buchRepository = buchRepository;
        this.schöpferRepository = schöpferRepository;
    }

    //public void queryExample(){}

    /*public static void main(String[] args) {
        SpringApplication.run(BookFinderApplication.class, args);

        //System.out.println("Soy Book Finder y estoy en proceso de desarrollo");
    }
}
*/
