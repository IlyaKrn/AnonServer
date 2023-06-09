package com.example.anonserver.api;

import com.example.anonserver.domain.models.PostModel;
import com.example.anonserver.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

@RestController
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;


    private final String SORT_BY_DATE = "by_date";
    private final String SORT_BY_LIKES = "by_likes";


    @GetMapping("getPostList")
    public List<PostModel> getPostList(
            @RequestParam(value = "count", defaultValue = "20") int count,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "tag", defaultValue = "") String tag,
            @RequestParam(value = "sort", defaultValue = SORT_BY_DATE) String sort
    ) {
        ArrayList<PostModel> posts = new ArrayList<>();
        for (PostModel p : postRepository.findByIsBanned(false)) {
            if (!tag.isEmpty()){
                if (Arrays.asList(p.getTags()).contains(tag)) {
                    posts.add(p);
                }
            }
            else {
                posts.add(p);
            }
        }
        posts.sort(new Comparator<PostModel>() {
            @Override
            public int compare(PostModel o1, PostModel o2) {
                switch (sort) {
                    case SORT_BY_DATE:
                        return Long.compare(o2.getUploadTime(), o1.getUploadTime());
                    case SORT_BY_LIKES:
                        return Long.compare(o1.getLikesIds().length, o2.getLikesIds().length);
                }
                return 0;
            }
        });

        return posts.subList(offset, offset + count);
    }

    @GetMapping("getPostById")
    public PostModel getPostById(
            @RequestParam("id") long id
    ){
        return postRepository.findById(id).get();
    }
    @GetMapping("getPostByAuthor")
    public List<PostModel> getPostByAuthor(
            @RequestParam("author_is") long id,
            @RequestParam(value = "count", defaultValue = "20") int count,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "tag", defaultValue = "") String tag,
            @RequestParam(value = "sort", defaultValue = SORT_BY_DATE) String sort
    ){
        ArrayList<PostModel> posts = new ArrayList<>();
        for (PostModel p : postRepository.findByIsBanned(false)) {
            if (!tag.isEmpty()){
                if (Arrays.asList(p.getTags()).contains(tag)) {
                    posts.add(p);
                }
            }
            else {
                posts.add(p);
            }
        }
        posts.sort(new Comparator<PostModel>() {
            @Override
            public int compare(PostModel o1, PostModel o2) {
                switch (sort) {
                    case SORT_BY_DATE:
                        return Long.compare(o2.getUploadTime(), o1.getUploadTime());
                    case SORT_BY_LIKES:
                        return Long.compare(o1.getLikesIds().length, o2.getLikesIds().length);
                }
                return 0;
            }
        });
        return postRepository.findByAuthorId(id);
    }

}
