package com.example.anonserver.api.models.edit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditCommentRequest {
    private String text;
    private List<String> imagesUrls;
    private List<String> filesUrls;
}
