package com.example.anonserver.api.models.posts;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAdminSelfResponse {
    private long id;
    private long authorId;
    private String text;
    private List<Long> likesIds;
    private List<Long> commentsIds;
    private List<String> tags;
    private boolean isBanned;
    private boolean isDeleted;
    private long uploadTime;
    private boolean isEdited;
    private List<String> imagesUrls;
    private List<String> filesUrls;
}
