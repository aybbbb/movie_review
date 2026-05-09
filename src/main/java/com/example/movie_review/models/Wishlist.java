package com.example.movie_review.models;

import lombok.Data;

@Data
public class Wishlist {
    private int id;
    private int memId;
    private String movieId;
    private int isDel;
    private String regDate;
    private String editDate;
}
