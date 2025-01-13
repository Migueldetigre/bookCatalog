package com.alurachallenges.literalura.bookfinder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bücher")
public class Buch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String titel;

    @ManyToOne()
    private Autor autor;

    private String nameAutor;
    private String sprache;
    private Double anzahlDerDownloads;

    public Buch(){

    }
    public Buch(BücherDaten bücherDaten){
        this.titel = bücherDaten.titel();
        this.nameAutor = sieheEarsteAutor(bücherDaten).getName();
        this.sprache = sieheEarsteSpreche(bücherDaten);
        this.anzahlDerDownloads = bücherDaten.anzahlDerDownloads();
    }

//Setters und Getters

    public Long getId(){
        return Id;
    }
    public void setId(Long id){
        Id = id;
    }

    public String getTitel(){
        return titel;
    }
    public void setTitel(String titel){
        this.titel = titel;
    }

    public String getNameAutor(){
        return nameAutor;
    }
    public void setNameAutor(String nameAutor){
        this.nameAutor = nameAutor;
    }

    public String getSprache(){
        return sprache;
    }
    public void setSprache(String sprache){
        this.sprache = sprache;
    }

    public Double getanzahlDerDownloads(){
        return anzahlDerDownloads;
    }
    public void setAnzahlDerDownloads(Double anzahlDerDownloads){
        this.anzahlDerDownloads = anzahlDerDownloads;
    }

    public Autor getAutor(){
        return autor;
    }
    public void setAutor(Autor autor){
        this.autor = autor;
    }


//Methods

public Autor sieheEarsteAutor(BücherDaten bücherDaten){
        AutorDaten autorDaten = bücherDaten.autor().get(0);
        return new Autor(autorDaten);
}

public String sieheEarsteSpreche(BücherDaten bücherDaten){
        String sprache = bücherDaten.sprache().toString();
        return sprache;
}

@Override
    public String toString(){
        return
                "Titulo = " + titel + "\'" +
                        ", Autor =" + nameAutor +
                        ", Idiomas =" + sprache +
                        " , Numero de descargas= " + anzahlDerDownloads;

    }
}
