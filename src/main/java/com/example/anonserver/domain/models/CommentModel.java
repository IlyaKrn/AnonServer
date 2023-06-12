package com.example.anonserver.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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
    @Column(columnDefinition = "bigint", nullable = false)
    private long id;
    @Column(columnDefinition = "bigint", nullable = false)
    private long authorId;
    @Column(columnDefinition = "text")
    private String text;
    @Column(columnDefinition = "text[]")
    private ArrayList<String> imagesUrls;
    @Column(columnDefinition = "text[]")
    private ArrayList<String> filesUrls;
    @Column(columnDefinition = "boolean", nullable = false)
    private boolean isBanned;

}
