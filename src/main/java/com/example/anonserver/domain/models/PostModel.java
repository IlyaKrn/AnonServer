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
    private long id;
    private long authorId;
    @ElementCollection
    private List<Long> likesIds;
    @ElementCollection
    private List<Long> commentsIds;
    @ElementCollection
    private List<String> tags;
    private boolean isBanned;
    private long uploadTime;
    private boolean isEdited;
    @ElementCollection
    private List<String> imagesUrls;
    @ElementCollection
    private List<String> filesUrls;


}
