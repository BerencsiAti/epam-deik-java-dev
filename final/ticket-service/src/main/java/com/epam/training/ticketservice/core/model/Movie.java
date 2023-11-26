package com.epam.training.ticketservice.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private String name;
    private String genre;
    private int length;

    public Movie(String name, String genre, int length) {
        this.name = name;
        this.genre = genre;
        this.length = length;
    }
}
