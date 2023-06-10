package com.example.anonserver.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long authorId;
    private ArrayList<Long> likesIds;
    private ArrayList<Long> commentsIds;
    private ArrayList<String> tags;
    private boolean isBanned;
    private long uploadTime;
    private boolean isEdited;
    private ArrayList<String> imagesUrls;
    private ArrayList<String> filesUrls;


}
