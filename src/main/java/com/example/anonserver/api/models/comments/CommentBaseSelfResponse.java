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
public class CommentBaseSelfResponse {
    private long id;
    private long authorId;
    private String text;
    private int likesCount;
    private List<String> imagesUrls;
    private List<String> filesUrls;
    private boolean isBanned;

}
