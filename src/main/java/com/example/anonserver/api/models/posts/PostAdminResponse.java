package com.example.anonserver.api.models.posts;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAdminResponse {
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
