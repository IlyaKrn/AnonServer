package com.example.anonserver.api;

import com.example.anonserver.domain.models.CommentModel;
import com.example.anonserver.domain.models.PostModel;
import com.example.anonserver.domain.models.Role;
import com.example.anonserver.domain.models.UserModel;
import com.example.anonserver.repositories.CommentRepository;
import com.example.anonserver.repositories.PostRepository;
import com.example.anonserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;
import java.util.function.Predicate;

@RestController
@RequestMapping("api/lists")
public class ListController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("recommendations")
    public ResponseEntity<ArrayList<Long>> recommendations(
            @RequestParam(value = "count", defaultValue = "100") int count,
            @RequestParam(value = "offset", defaultValue = "0") int offset
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ArrayList<PostModel> posts = (ArrayList<PostModel>) postRepository.findAll();
        posts.removeIf(new Predicate<PostModel>() {
            @Override
            public boolean test(PostModel postModel) {
                return postModel.isBanned() || postModel.isDeleted();
            }
        });
        posts.sort(new Comparator<PostModel>() {
            @Override
            public int compare(PostModel p1, PostModel p2) {
                return Long.compare(p1.getUploadTime(), p2.getUploadTime());
            }
        });
        Map<String, Integer> tags = new HashMap();
        UserModel user = userRepository.findByUsername(auth.getName()).get();

        for (PostModel p : posts){
            if (p.getLikesIds().contains(user.getId())){
                for(String s : p.getTags()){
                    int k = 1;
                    if(tags.get(s) != 0){
                        k += tags.get(s);
                    }
                    tags.put(s, k);
                }
            }
        }

        Map<Long, Integer> rating = new HashMap<>();
        for (PostModel p : posts){
            int k = 0;
            for (String s : p.getTags()){
                if(tags.get(s) != 0){
                    k += tags.get(s);
                }
            }
            rating.put(p.getId(), (int) (k * (System.currentTimeMillis() - p.getUploadTime())));
        }
        ArrayList<Map.Entry<Long, Integer>> temp = new ArrayList<>(rating.entrySet());
        temp.sort(new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        ArrayList<Long> response = new ArrayList<>();
        if (offset > temp.size()){
            return ResponseEntity.ok(new ArrayList<>());
        }
        else if(offset + count > temp.size()){
            for (int i = offset; i < temp.size(); i++){
                response.add(temp.get(i).getKey());
            }
        }
        else {
            for (int i = offset; i < count + offset; i++){
                response.add(temp.get(i).getKey());
            }
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("admUsers")
    public ResponseEntity<ArrayList<Long>> recommendations(
            @RequestParam(value = "count", defaultValue = "100") int count,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "search", defaultValue = "") String search
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().contains(Role.ADMIN)) {
            Map<String, Object> params = new HashMap<>();
            try{
                for(int i = 0; i < search.split(",").length; i++) {
                    params.put(search.split(",")[i].split("_")[0], search.split(",")[i].split("_")[1]);
                }
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            ArrayList<UserModel> users = (ArrayList<UserModel>) userRepository.findAll();
            ArrayList<Long> response = new ArrayList<>();
            for(UserModel u : users){
                if (params.containsKey("banned") && !params.get("banned").equals(u.isBanned()))
                    continue;
                if (params.containsKey("subscriber") && !u.getSubscribersIds().contains(params.get("subscriber")))
                    continue;
                if (params.containsKey("role") && !u.getRoles().contains(Role.getFromString((String) params.get("role"))))
                    continue;
                response.add(u.getId());

            }
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("admPosts")
    public ResponseEntity<ArrayList<Long>> posts(
            @RequestParam(value = "count", defaultValue = "100") int count,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "search", defaultValue = "") String search
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().contains(Role.ADMIN)) {
            Map<String, Object> params = new HashMap<>();
            try{
                for(int i = 0; i < search.split(",").length; i++) {
                    params.put(search.split(",")[i].split("_")[0], search.split(",")[i].split("_")[1]);
                }
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            ArrayList<PostModel> posts = (ArrayList<PostModel>) postRepository.findAll();
            ArrayList<Long> response = new ArrayList<>();
            for(PostModel p : posts){
                if (params.containsKey("banned") && !params.get("banned").equals(p.isBanned()))
                    continue;
                if (params.containsKey("authorId") && !params.get("authorId").equals(p.getAuthorId()))
                    continue;
                if (params.containsKey("likesId") && !p.getLikesIds().contains(params.get("likesId")))
                    continue;
                if (params.containsKey("commentsId") && !p.getCommentsIds().contains(params.get("commentsId")))
                    continue;
                if (params.containsKey("tag") && !p.getTags().contains(params.get("tag")))
                    continue;
                if (params.containsKey("deleted") && !params.get("deleted").equals(p.isDeleted()))
                    continue;
                response.add(p.getId());

            }
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("admComments")
    public ResponseEntity<ArrayList<Long>> admComments(
            @RequestParam(value = "count", defaultValue = "100") int count,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "search", defaultValue = "") String search
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().contains(Role.ADMIN)) {
            Map<String, Object> params = new HashMap<>();
            try{
                for(int i = 0; i < search.split(",").length; i++) {
                    params.put(search.split(",")[i].split("_")[0], search.split(",")[i].split("_")[1]);
                }
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            ArrayList<CommentModel> comments = (ArrayList<CommentModel>) commentRepository.findAll();
            ArrayList<Long> response = new ArrayList<>();
            for(CommentModel c : comments){
                if (params.containsKey("banned") && !params.get("banned").equals(c.isBanned()))
                    continue;
                if (params.containsKey("deleted") && !params.get("deleted").equals(c.isDeleted()))
                    continue;
                if (params.containsKey("authorId") && !params.get("authorId").equals(c.getAuthorId()))
                    continue;
                if (params.containsKey("likesId") && !c.getLikesIds().contains(params.get("likesId")))
                    continue;
                response.add(c.getId());

            }
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
