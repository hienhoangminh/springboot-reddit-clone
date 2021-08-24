package com.hienhoang.springboot.springredditclone.service;

import com.hienhoang.springboot.springredditclone.dto.PostRequest;
import com.hienhoang.springboot.springredditclone.dto.PostResponse;
import com.hienhoang.springboot.springredditclone.exception.PostNotFoundException;
import com.hienhoang.springboot.springredditclone.exception.SubredditNotFoundException;
import com.hienhoang.springboot.springredditclone.mapper.PostMapper;
import com.hienhoang.springboot.springredditclone.model.Post;
import com.hienhoang.springboot.springredditclone.model.Subreddit;
import com.hienhoang.springboot.springredditclone.model.User;
import com.hienhoang.springboot.springredditclone.repository.PostRepository;
import com.hienhoang.springboot.springredditclone.repository.SubRedditRepository;
import com.hienhoang.springboot.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final SubRedditRepository subRedditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void save(PostRequest postRequest){
        Subreddit subreddit = subRedditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found!"));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subRedditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Sub reddit with id " + id + " not found!"));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + name + " not found!"));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

}
