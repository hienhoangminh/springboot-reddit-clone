package com.hienhoang.springboot.springredditclone.controller;

import com.hienhoang.springboot.springredditclone.dto.SubredditDto;
import com.hienhoang.springboot.springredditclone.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public SubredditDto createSubreddit(@RequestBody SubredditDto subredditDto){
        return subredditService.save(subredditDto);
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits(){

        return ResponseEntity.status(HttpStatus.OK)
        .body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubredditById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getSubredditById(id));
    }

}
