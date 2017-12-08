package com.example.demo.spring.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Movie {
    private String id;
    private String movieName;
    private String movieLink;

    public Movie(String movieName, String movieLink) {
        this.movieName = movieName;
        this.movieLink = movieLink;
    }
}
