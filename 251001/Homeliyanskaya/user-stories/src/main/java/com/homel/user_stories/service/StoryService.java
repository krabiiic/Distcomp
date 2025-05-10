package com.homel.user_stories.service;

import com.homel.user_stories.dto.StoryRequestTo;
import com.homel.user_stories.dto.StoryResponseTo;
import com.homel.user_stories.exception.EntityNotFoundException;
import com.homel.user_stories.mapper.StoryMapper;
import com.homel.user_stories.model.Story;
import com.homel.user_stories.model.User;
import com.homel.user_stories.repository.StoryRepository;
import com.homel.user_stories.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoryService {
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    @Autowired
    public StoryService(StoryRepository storyRepository, UserRepository userRepository) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
    }

    public StoryResponseTo createStory(StoryRequestTo storyRequest) {
        Story story = StoryMapper.INSTANCE.toEntity(storyRequest);

        User user = userRepository.findById(storyRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        story.setUser(user);

        Story savedStory =storyRepository.save(story);
        return StoryMapper.INSTANCE.toResponse(savedStory);
    }

    public StoryResponseTo getStory(Long id) {
        return storyRepository.findById(id)
                .map(StoryMapper.INSTANCE::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Story not found"));
    }

    public List<StoryResponseTo> getAllStories() {
        return storyRepository.findAll().stream()
                .map(StoryMapper.INSTANCE::toResponse)
                .toList();
    }

    public void deleteStory(Long id) {
        storyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Story with id " + id + " not found"));

        storyRepository.deleteById(id);
    }

    public StoryResponseTo updateStory(StoryRequestTo storyRequest) {
        Story existingStory = storyRepository.findById(storyRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Story not found"));

        User user = userRepository.findById(storyRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existingStory.setUser(user);
        existingStory.setTitle(storyRequest.getTitle());
        existingStory.setContent(storyRequest.getContent());

        Story updatedStory = storyRepository.save(existingStory);

        return StoryMapper.INSTANCE.toResponse(updatedStory);
    }
}
