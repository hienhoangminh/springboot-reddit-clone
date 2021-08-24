package com.hienhoang.springboot.springredditclone.controller;

import com.hienhoang.springboot.springredditclone.dto.CommentDto;
import com.hienhoang.springboot.springredditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comments/")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity createComment(@RequestBody CommentDto commentDto){
        commentService.save(commentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("by-post/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByPostId(id));
    }

    @GetMapping("by-user/{userName}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByUsername(@PathVariable String userName){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByUsername(userName));
    }
}
