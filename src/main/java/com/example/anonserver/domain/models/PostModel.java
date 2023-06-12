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
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "bigint", nullable = false)
    private long id;
    @Column(columnDefinition = "bigint", nullable = false)
    private long authorId;
    @Column(columnDefinition = "bigint[]")
    private ArrayList<Long> likesIds;
    @Column(columnDefinition = "bigint[]")
    private ArrayList<Long> commentsIds;
    @Column(columnDefinition = "text[]")
    private ArrayList<String> tags;
    @Column(columnDefinition = "boolean", nullable = false)
    private boolean isBanned;
    @Column(columnDefinition = "bigint", nullable = false)
    private long uploadTime;
    @Column(columnDefinition = "boolean", nullable = false)
    private boolean isEdited;
    @Column(columnDefinition = "text[]")
    private ArrayList<String> imagesUrls;
    @Column(columnDefinition = "text[]")
    private ArrayList<String> filesUrls;


}
