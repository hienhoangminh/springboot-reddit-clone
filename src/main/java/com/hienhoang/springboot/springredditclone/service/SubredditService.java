package com.hienhoang.springboot.springredditclone.service;

import com.hienhoang.springboot.springredditclone.dto.SubredditDto;
import com.hienhoang.springboot.springredditclone.exception.SpringRedditException;
import com.hienhoang.springboot.springredditclone.mapper.SubredditMapper;
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
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save = subRedditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }


    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subRedditRepository.findAll().stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubredditById(Long id) {
        Subreddit subreddit = subRedditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with id " + id));

        return subredditMapper.mapSubredditToDto(subreddit);

    }

    // We don't need to manually do the mapping now
//    private SubredditDto mapToDto(Subreddit subReddit){
//        return SubredditDto.builder()
//                .name(subReddit.getName())
//                .id(subReddit.getId())
//                .numberOfPosts(subReddit.getPosts().size())
//                .build();
//    }

//    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//        return Subreddit.builder().name(subredditDto.getName())
//                .description(subredditDto.getDescription())
//                .build();
//    }
}
