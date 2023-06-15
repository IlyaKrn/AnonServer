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
    private long id;
    private long authorId;
    private String text;
    @ElementCollection
    private List<Long> likesIds;
    @ElementCollection
    private List<String> imagesUrls;
    @ElementCollection
    private List<String> filesUrls;
    private long uploadTime;
    private boolean isEdited;
    private boolean isBanned;
    private boolean isDeleted;

}
