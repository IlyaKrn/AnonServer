package com.example.anonserver.domain.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long authorId;
    private String text;
    private String[] imagesUrls;
    private String[] filesUrls;
    private boolean isBanned;

}
