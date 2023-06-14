package com.example.anonserver.api.models.posts;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostBaseSelfResponse {
    private long id;
    private long authorId;
    private int likesCount;
    private List<Long> commentsIds;
    private List<String> tags;
    private boolean isBanned;
    private long uploadTime;
    private boolean isEdited;
    private List<String> imagesUrls;
    private List<String> filesUrls;

}
