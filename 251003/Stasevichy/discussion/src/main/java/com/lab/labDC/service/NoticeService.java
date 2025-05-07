package com.lab.labDC.service;

import com.lab.labDC.dto.NoticeRequestTo;
import com.lab.labDC.dto.NoticeResponseTo;
import com.lab.labDC.entity.Notice;
import com.lab.labDC.repository.NoticeRepositoryCassandra;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepositoryCassandra noticeRepositoryCassandra;
    private boolean checkMult = false;
    public List<NoticeResponseTo> getAllNotices() {
        return noticeRepositoryCassandra.findAll().stream()
                .map(notice -> new NoticeResponseTo(notice.getId(), notice.getContent(), notice.getStoryId()))
                .collect(Collectors.toList());
    }

    public NoticeResponseTo getNoticeById(Long id) {
        return noticeRepositoryCassandra.findById(id).map(notice -> new NoticeResponseTo(notice.getId(), notice.getContent(), notice.getStoryId()))
                .orElseThrow(() -> new NoSuchElementException("Notice not found"));
    }

    @Transactional
    public NoticeResponseTo createNotice(Notice notices) {
        try {
            Notice saveN = noticeRepositoryCassandra.save(notices);

        } catch (Exception e){
            System.out.println(e.toString());
        }
        return new NoticeResponseTo(notices.getId(), notices.getContent(), notices.getStoryId());
    }

    public NoticeResponseTo updateNotice(Long id, NoticeRequestTo request) {
        Notice notice = noticeRepositoryCassandra.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notice not found"));
        notice.setContent(request.getContent());
        notice.setStoryId(request.getStoryId());
        noticeRepositoryCassandra.save(notice);
        return new NoticeResponseTo(id, notice.getContent(), notice.getStoryId());
    }

    public void deleteNotice(Long id) {
        Notice notices = noticeRepositoryCassandra.findById(id).orElseThrow(() -> new NoSuchElementException("Notice not found"));
        noticeRepositoryCassandra.deleteById(id);
    }
}
