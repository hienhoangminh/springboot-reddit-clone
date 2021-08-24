package com.hienhoang.springboot.springredditclone.controller;

import com.hienhoang.springboot.springredditclone.dto.PostRequest;
import com.hienhoang.springboot.springredditclone.dto.PostResponse;
import com.hienhoang.springboot.springredditclone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest){
        // Save the post & return status code = 201
        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id){
        return status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id){
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }


    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUserName(@PathVariable String name){
        return status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }

}
