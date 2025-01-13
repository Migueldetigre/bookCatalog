package com.alurachallenges.literalura.bookfinder.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

@Entity
@Table(name = "autoren")

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String name;
    private Integer BirthDate;
    private Integer DeathDate;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Buch> bücher;

    public Autor(){

    }

    public Autor(AutorDaten autorDaten){
        this.name = autorDaten.name();
        this.BirthDate = Integer.valueOf(autorDaten.birthDate());
        this.DeathDate = Integer.valueOf(autorDaten.deathDate);
    }

    public Autor(autorDaten autorDaten) {

    }

//Getters und Setters

    public List<Buch> getBuch(){
        return bücher;
    }

    public void setBücher(List<Buch> bücher){
        this.bücher = bücher;
    }

    public Long getId(){
        return Id;
    }

    public void setId(Long id){
        Id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getBirthDate(){
        return birthDate;
    }

    public void setBirthDate(Integer birthDate){
        this.BirthDate = birthDate;
    }

    public Integer getDeathDate(){
        return deathDate;
    }

    public Autor sieheEarsteAutor(BücherDaten bücherDaten){
        autorDaten autorDaten = bücherDaten.autor().get(0);
        return new Autor(autorDaten);
    }

    @Override
    public String toString(){
        return
                "Nombre: " + name + "\'" +
                        ", Año de Nacimiento: " + getBirthDate() +
                        ", Año de fallecimiento: " + getDeathDate();
    }
}
