package com.example.anonserver.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long authorId;
    private String text;
    private ArrayList<String> imagesUrls;
    private ArrayList<String> filesUrls;
    private boolean isBanned;

}
