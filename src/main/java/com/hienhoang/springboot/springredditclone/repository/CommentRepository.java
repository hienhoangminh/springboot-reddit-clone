package com.hienhoang.springboot.springredditclone.repository;

import com.hienhoang.springboot.springredditclone.model.Comment;
import com.hienhoang.springboot.springredditclone.model.Post;
import com.hienhoang.springboot.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
