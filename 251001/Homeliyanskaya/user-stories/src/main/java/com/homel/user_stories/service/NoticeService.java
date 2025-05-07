package com.homel.user_stories.service;

import com.homel.user_stories.dto.NoticeRequestTo;
import com.homel.user_stories.dto.NoticeResponseTo;
import com.homel.user_stories.exception.EntityNotFoundException;
import com.homel.user_stories.mapper.NoticeMapper;
import com.homel.user_stories.model.Notice;
import com.homel.user_stories.model.Story;
import com.homel.user_stories.repository.NoticeRepository;
import com.homel.user_stories.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
        this.noticeRepository = noticeRepository;
    }

    public NoticeResponseTo createNotice(NoticeRequestTo noticeRequest) {
        Notice notice = NoticeMapper.INSTANCE.toEntity(noticeRequest);

        Story story = storyRepository.findById(noticeRequest.getStoryId())
                .orElseThrow(() -> new EntityNotFoundException("Story not found"));

        notice.setStory(story);

        Notice savedNotice = noticeRepository.save(notice);
        return NoticeMapper.INSTANCE.toResponse(savedNotice);
    }

    public NoticeResponseTo getNotice(Long id) {
        return noticeRepository.findById(id)
                .map(NoticeMapper.INSTANCE::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Notice not found"));
    }

    public List<NoticeResponseTo> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(NoticeMapper.INSTANCE::toResponse)
                .toList();
    }

    public void deleteNotice(Long id) {
        noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notice with id " + id + " not found"));

        noticeRepository.deleteById(id);
    }

    public NoticeResponseTo updateNotice(NoticeRequestTo noticeRequest) {
        Notice existingNotice = noticeRepository.findById(noticeRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Notice not found"));

        Story story = storyRepository.findById(noticeRequest.getStoryId())
                .orElseThrow(() -> new EntityNotFoundException("Story not found"));

        existingNotice.setStory(story);
        existingNotice.setContent(noticeRequest.getContent());

        Notice updatedNotice = noticeRepository.save(existingNotice);

        return NoticeMapper.INSTANCE.toResponse(updatedNotice);
    }
}
