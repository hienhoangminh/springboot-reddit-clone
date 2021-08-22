package com.hienhoang.springboot.springredditclone.mapper;

import com.hienhoang.springboot.springredditclone.dto.SubredditDto;
import com.hienhoang.springboot.springredditclone.model.Post;
import com.hienhoang.springboot.springredditclone.model.Subreddit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts){return numberOfPosts.size();}

}
