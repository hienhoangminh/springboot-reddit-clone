package com.hienhoang.springboot.springredditclone.service;

import com.hienhoang.springboot.springredditclone.dto.SubredditDto;
import com.hienhoang.springboot.springredditclone.model.Subreddit;
import com.hienhoang.springboot.springredditclone.repository.SubRedditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubRedditRepository subRedditRepository;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save = subRedditRepository.save(mapSubredditDto(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subRedditRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SubredditDto mapToDto(Subreddit subReddit){
        return SubredditDto.builder()
                .name(subReddit.getName())
                .id(subReddit.getId())
                .postCount(subReddit.getPosts().size())
                .build();
    }
}
