package com.hienhoang.springboot.springredditclone.service;

import com.hienhoang.springboot.springredditclone.dto.CommentDto;
import com.hienhoang.springboot.springredditclone.exception.PostNotFoundException;
import com.hienhoang.springboot.springredditclone.mapper.CommentMapper;
import com.hienhoang.springboot.springredditclone.model.NotificationEmail;
import com.hienhoang.springboot.springredditclone.model.Post;
import com.hienhoang.springboot.springredditclone.model.User;
import com.hienhoang.springboot.springredditclone.repository.CommentRepository;
import com.hienhoang.springboot.springredditclone.repository.PostRepository;
import com.hienhoang.springboot.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentDto commentDto){
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post with id " + commentDto.getPostId() + " not found"));
        commentRepository.save(commentMapper.map(commentDto, post, authService.getCurrentUser()));

        String message = mailContentBuilder.build(commentDto.getUserName() + " posted a comment on your post: " + post.getUrl());
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail("Someone commented on your post ", user.getUsername(), message));
    }


    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));
        return commentRepository.findByPost(post).stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByUsername(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User with name " + userName + " not found"));
        return commentRepository.findAllByUser(user).stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
