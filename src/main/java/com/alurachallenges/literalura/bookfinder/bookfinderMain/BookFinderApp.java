package com.alurachallenges.literalura.bookfinder.bookfinderMain;

import com.alurachallenges.literalura.bookfinder.model.*;
import com.alurachallenges.literalura.bookfinder.bookfinderRepository.BuchRepository;
import com.alurachallenges.literalura.bookfinder.bookfinderRepository.SchöpferRepository;
import com.alurachallenges.literalura.bookfinder.service.BookFinderConsume;
import com.alurachallenges.literalura.bookfinder.service.UmwandelnDaten;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.Scanner;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class BookFinderApp {
    private static final String adresseVerbindung = "https://gutendex.com/books/";
    private UmwandelnDaten konverter = new UmwandelnDaten();
    private BookFinderConsume query = new BookFinderConsume();
    private int UiOptione = -1;
    private SchöpferRepository schöpferRepository;
    private BuchRepository buchRepository;
    List<Autor> autoren;
    List<Buch> bücher;
    Scanner tastaturEingabe = new Scanner(System.in);

    public BookFinderApp(SchöpferRepository schöpferRepository, BuchRepository buchRepository) {
        this.buchRepository = buchRepository;
        this.schöpferRepository = schöpferRepository;
    }

    public void queryExample() {
        do {
            dasMenu();
            UiOptione = Integer.valueOf(tastaturEingabe.nextLine());
            switch (UiOptione) {
                case 1:
                    searchBookFinder();
                    break;
                case 2:
                    showMeBookFinderQuery();
                    break;
                case 3:
                    showMeSearchedAutors();
                    break;
                case 4:
                    showMeYearAutors();
                    break;
                case 5:
                    showMeLanguageQueryBooks();
                    break;
                case 0:
                    System.out.println("¡Gracias por elegirnos! Vuelva prontos!!!");
                    break;
                default:
                    System.out.println("La opción ingresada no es válida. Ingrese una opción del 0 al 5");
            }
        } while (UiOptione != 0);
    }

    public static void dasMenu() {
        System.out.println("""
                Ingrese una opción del 0 al 5:
                
                1- Busqueda de libro por titulo
                2- Ver lista de libros registrados
                3- Ver lista de autores registrados
                4- Ver lista de autores vivos en un determinado año
                5- Ver lista de libros por idioma
                    
                0- Salir
                """);
    }

    public void uiLanguageMenu() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                en- ingles
                de- Aleman
                es- Español
                fr- Frances
                pt- Portugues
                """);
    }

    private void searchBookFinder() {
        System.out.println("Escriba el nombre del libro que esta buscando");
        String userQuery = tastaturEingabe.nextLine();

        String search = "?search=" + userQuery.replace(" ", "+");
        var json = query.abrufenDaten(adresseVerbindung + search);
        System.out.println(json);
        var daten = konverter.abrufenDaten(json, Daten.class);

        BücherDaten bücherDaten = daten.ergebnisse().get(0);
        Buch buch = new Buch(bücherDaten);
        Autor autor = new Autor().sieheEarsteAutor(bücherDaten);

        System.out.println(buch);
        saveBookFinderAutor(buch, autor);
    }

    private void saveBookFinderAutor(Buch buch, Autor autor) {
        // Autores guardados en base de datos Por nombre
        Optional<Autor> autorQuery = schöpferRepository.findByNameContains(autor.getName());

        // Almacena el autor si no existe en la DB
        if (autorQuery.isPresent()) {
            System.out.println("Este Autor ya se encuentra guardado");
            buch.setAutor(autorQuery.get());
        } else {
            System.out.println("Guardar nuevo autor");
            schöpferRepository.save(autor);
            buch.setAutor(autor);
        }

        // Almacenar el libro
        try {
            buchRepository.save(buch);
        } catch (Exception e) {
            // Manejo de excepciones
            System.out.println("Error inesperado al guardar el libro en su biblioteca: " + e.getMessage());
        }
    }

    // Mostrar lista de libros ya buscados
    private void showMeBookFinderQuery() {
        // Consulta de base de datos local
        bücher = buchRepository.findAll();
        // Mostrar lista guardada
        printBooksByName(bücher);
    }

    private void showMeSearchedAutors() {
        // Realizamos consulta a base de datos local
        autoren = schöpferRepository.findAll();
        // Mostrar la lista guardada de autores
        printAutorByName(autoren);
    }

    // Mostrar lista de autores vivos por determinado año
    private void showMeYearAutors() {
        System.out.println("Escriba el año del autor que esta buscando");
        Integer year = Integer.valueOf(tastaturEingabe.nextLine());

        autoren = schöpferRepository.findByBirthDateLessThanEqualAndDeathDateGreaterThanEqual(year, year);
        if (autoren.isEmpty()) {
            System.out.println("No hay autores en esta busqueda");
        } else {
            printAutorByName(autoren);
        }
    }

    private void printAutorByName(List<Autor> autoren) {
        autoren.stream()
                .sorted(Comparator.comparing(Autor::getName))
                .forEach(System.out::println);
    }

    private void printBooksByName(List<Buch> bücher) {
        bücher.stream()
                .sorted(Comparator.comparing(Buch::getTitel))
                .forEach(System.out::println);
    }

    private void showMeLanguageQueryBooks() {
        uiLanguageMenu();
        String language = tastaturEingabe.nextLine();
        String languageNom;
        if (language.length() >= 2) {
            languageNom = language.substring(0, 2);
        } else {
            // Validación de los caracteres ingresados por el usuario
            languageNom = language;
        }
        bücher = buchRepository.findBySpracheContains(languageNom);
        if (bücher.isEmpty()) {
            System.out.println("No se encontraron libros en esta busqueda. Pruebe otro idioma");
        } else {
            printBooksByName(bücher);
        }
    }

    public static void main(String[] args) {
        // Inicializar repositorios
        SchöpferRepository schöpferRepository = new SchöpferRepository() {
            @Override
            public List<Autor> findAll(Sort sort) {
                return List.of();
            }

            @Override
            public Page<Autor> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Autor> S save(S entity) {
                return null;
            }

            @Override
            public <S extends Autor> List<S> saveAll(Iterable<S> entities) {
                return List.of();
            }

            @Override
            public Optional<Autor> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public List<Autor> findAll() {
                return List.of();
            }

            @Override
            public List<Autor> findAllById(Iterable<Long> longs) {
                return List.of();
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Autor entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends Autor> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Autor> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends Autor> List<S> saveAllAndFlush(Iterable<S> entities) {
                return List.of();
            }

            @Override
            public void deleteAllInBatch(Iterable<Autor> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Autor getOne(Long aLong) {
                return null;
            }

            @Override
            public Autor getById(Long aLong) {
                return null;
            }

            @Override
            public Autor getReferenceById(Long aLong) {
                return null;
            }

            @Override
            public <S extends Autor> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Autor> List<S> findAll(Example<S> example) {
                return List.of();
            }

            @Override
            public <S extends Autor> List<S> findAll(Example<S> example, Sort sort) {
                return List.of();
            }

            @Override
            public <S extends Autor> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Autor> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Autor> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends Autor, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }

            @Override
            public Optional<Autor> findByNameContains(String nameAutor) {
                return Optional.empty();
            }

            @Override
            public List<Autor> findByBirthDateLessThanEqualAndDeathDateGreaterThanEqual(Integer BirthDate, Integer DeathDate) {
                return List.of();
            }
        };
        BuchRepository buchRepository = new BuchRepository() {
            @Override
            public List<Buch> findBySpracheContains(String sprache) {
                return List.of();
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Buch> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends Buch> List<S> saveAllAndFlush(Iterable<S> entities) {
                return List.of();
            }

            @Override
            public void deleteAllInBatch(Iterable<Buch> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Buch getOne(Long aLong) {
                return null;
            }

            @Override
            public Buch getById(Long aLong) {
                return null;
            }

            @Override
            public Buch getReferenceById(Long aLong) {
                return null;
            }

            @Override
            public <S extends Buch> List<S> findAll(Example<S> example) {
                return List.of();
            }

            @Override
            public <S extends Buch> List<S> findAll(Example<S> example, Sort sort) {
                return List.of();
            }

            @Override
            public <S extends Buch> List<S> saveAll(Iterable<S> entities) {
                return List.of();
            }

            @Override
            public List<Buch> findAll() {
                return List.of();
            }

            @Override
            public List<Buch> findAllById(Iterable<Long> longs) {
                return List.of();
            }

            @Override
            public <S extends Buch> S save(S entity) {
                return null;
            }

            @Override
            public Optional<Buch> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Buch entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends Buch> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public List<Buch> findAll(Sort sort) {
                return List.of();
            }

            @Override
            public Page<Buch> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Buch> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Buch> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Buch> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Buch> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends Buch, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }
        };

        // Crear instancia de BookFinderApp
        BookFinderApp app = new BookFinderApp(schöpferRepository, buchRepository);

        // Llamar al método queryExample()
        app.queryExample();
    }
}

/*
public class BookFinderApp{
    private static final String adresseVerbindung = "https://gutendex.com/books/";
    private UmwandelnDaten konverter = new UmwandelnDaten();
    private BookFinderConsume query = new BookFinderConsume();
    private int UiOptione = -1;
    private SchöpferRepository schöpferRepository;
    private BuchRepository buchRepository;
    List<Autor> autoren;
    List<Buch> bücher;
    Scanner tastaturEingabe = new Scanner(System.in);

    public BookFinderApp(SchöpferRepository schöpferRepository, BuchRepository buchRepository) {
        this.buchRepository = buchRepository;
        this.schöpferRepository = schöpferRepository;
    }

    public static void queryExample(){
        do {
            dasMenu();
            UiOptione = Integer.valueOf(tastaturEingabe.nextLine());
            switch (UiOptione){
                case 1:
                    searchBookFinder();
                    break;
                case 2:
                    showMeBookFinderQuery();
                    break;
                case 3:
                    showMeSearchedAutors();
                    break;
                case 4:
                    showMeYearAutors();
                    break;
                case 5:
                    showMeLanguageQueryBooks();
                    break;
                case 0:
                    System.out.println("¡Gracias por elegirnos! Vuelva prontos!!!");;
                    break;
                default:
                    System.out.println("La opción ingresada no es válida. Ingrese una opción del 0 al 5");
            }
        } while (UiOptione != 0);
    }

    public void dasMenu(){
        System.out.println("""
                Ingrese una opción del 0 al 5:
                
                1- Busqueda de libro por titulo
                2- Ver lista de libros registrados
                3- Ver lista de autores registrados
                4- Ver lista de autores vivos en un determinado año
                5- Ver lista de libros por idioma
                    
                0- Salir
                """);
    }

    public void uiLanguageMenu(){
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                en- ingles
                de- Aleman
                es- Español
                fr- Frances
                pt- Portugues
                """);
    }

    private void searchBookFinder(){
        System.out.println("Escriba el nombre del libro que esta buscando");
        String userQuery = tastaturEingabe.nextLine();

        String search = "?search=" + userQuery.replace(" ", "+");
        var json = query.abrufenDaten(adresseVerbindung + search);
        System.out.println(json);
        var daten = konverter.abrufenDaten(json, Daten.class);

        BücherDaten bücherDaten = daten.ergebnisse().get(0);
        Buch buch = new Buch(bücherDaten);
        Autor autor = new Autor().sieheEarsteAutor(bücherDaten);

        System.out.println(buch);
        saveBookFinderAutor(buch, autor);
    }

    private void saveBookFinderAutor(Buch buch, Autor autor){
        //Autores guardados en base de datos Por nombre
        Optional<Autor> autorQuery = SchöpferRepository.findByNameContains(autor.getName());

        //Almacena el autor si no existe en la DB
        if (autorQuery.isPresent()){
            System.out.println("Este Autor ya se encuentra guardado");
            buch.setAutor(autorQuery.get());
        } else {
            System.out.println("Guardar nuevo autor");
            SchöpferRepository.save(autor);
            buch.setAutor(autor);
        }

        //Almacenar el libro
        try{
            buchRepository.save(buch);
        } catch (Exception e){
            //Manejo de excepciones
            System.out.println("Error inesperado al guardar el libro en su biblioteca" + e.getMessage());
        }
    }

    //Mostrar lista de libros ya buscados
    private void showMeBookFinderQuery(){
        //Consulta de base de datos local
        bücher = buchRepository.findAll();
        //Mostrar lista guardada
        printBooksByName(bücher);
    }

    private void showMeSearchedAutors(){
        //Realizamos consulta a base de datos local
        autoren = SchöpferRepository.findAll();
        //Mostrar la lista guardada de autores
        printAutorByName(autoren);
    }

    //Mostrar lista de autores vivos por determinado año
    private void showMeYearAutors(){
        System.out.println("Escriba el año del autor que esta buscando");
        Integer year = Integer.valueOf(tastaturEingabe.nextLine());

        autoren = SchöpferRepository.findByBirthDateLessThanEqualAndDeathDateGreaterThanEqual(year, year);
        if (autoren.isEmpty()){
            System.out.println("No hay autores en esta busqueda");
        } else {
            printAutorByName(autoren);
        }
    }

    private void printAutorByName(List<Autor> autoren){
        autoren.stream()
                .sorted(Comparator.comparing(Buch::getNameAutor))
                .forEach(System.out::println);
    }

    private void printBooksByName(List<Buch> bücher){
        bücher.stream()
                .sorted(Comparator.comparing(Buch::getNameAutor))
                .forEach(System.out::println);
    }

    private void showMeLanguageQueryBooks(){
        uiLanguageMenu();
        String language = tastaturEingabe.nextLine();
        String languageNom;
        if (language.length()>= 2){
            languageNom = language.substring(0, 2);
        } else {
            //Validacion de los caracteres ingresados por el usuario
            languageNom = language;
        }
        bücher = buchRepository.findBySpracheContains(languageNom);
        if (bücher.isEmpty()){
            System.out.println("No se encontraron libros en esta busqueda. Pruebe otro idioma");
        } else {
            printBooksByName(bücher);
        }
    }
}
*/
/**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**/