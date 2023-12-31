package com.example.anonserver.api.models.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentBaseResponse {

    private long id;
    private String text;
    private int likesCount;
    private boolean isLiked;
    private List<String> imagesUrls;
    private List<String> filesUrls;

}
