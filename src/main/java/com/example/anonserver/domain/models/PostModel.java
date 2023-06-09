package com.example.anonserver.domain.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "posts")
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long authorId;
    private long[] likesIds;
    private long[] commentsIds;
    private String[] tags;
    private boolean isBanned;
    private long uploadTime;
    private boolean isEdited;
    private String[] imagesUrls;
    private String[] filesUrls;


}
