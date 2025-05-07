package org.ex.distributed_computing.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.distributed_computing.config.DiscussionCommunicationProps;
import org.ex.distributed_computing.dto.request.NoticeRequestDTO;
import org.ex.distributed_computing.dto.response.NoticeResponseDTO;
import org.ex.distributed_computing.exception.NotFoundException;
import org.ex.distributed_computing.kafka.notice.NoticeKafkaService;
import org.ex.distributed_computing.mapper.NoticeMapper;
import org.ex.distributed_computing.model.EntityStatus;
import org.ex.distributed_computing.repository.NewsRepository;
import org.ex.distributed_computing.repository.NoticeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

  private static final String STUB_COUNTRY = "US"; // since not supplied

  private final DiscussionCommunicationProps discussionProperties;
  private final NoticeRepository noticeRepository;
  private final NewsRepository newsRepository;
  private final NoticeKafkaService noticeKafkaService;
  private final RestTemplate restTemplate;
  private final NoticeMapper noticeMapper;

  public List<NoticeResponseDTO> getAllNotices() {
    return restTemplate.exchange(
        "%s%s/notices".formatted(discussionProperties.getAddress(), discussionProperties.getBaseApiPath()),
        HttpMethod.GET,
        HttpEntity.EMPTY,
        new ParameterizedTypeReference<List<NoticeResponseDTO>>() {
        }
    ).getBody();
  }

  @Cacheable(value = "notices", key = "#id")
  public NoticeResponseDTO getNoticeById(Long id) {
    log.info("Get notice by id: {}", id);
    return restTemplate.getForObject(
        "%s%s/notices/%d".formatted(discussionProperties.getAddress(), discussionProperties.getBaseApiPath(), id),
        NoticeResponseDTO.class
    );
  }

  @CachePut(value = "notices", key = "#result.id()")
  public NoticeResponseDTO createNotice(NoticeRequestDTO requestDTO) {
    newsRepository.findById(requestDTO.newsId())
        .orElseThrow(() -> new NotFoundException("News not found"));

    Long nextNoticeId = noticeRepository.nextId();
    var noticeKafkaMessage = new NoticeRequestDTO(nextNoticeId, requestDTO.newsId(), requestDTO.content(), EntityStatus.PENDING, STUB_COUNTRY);

    log.info("Sending new notice message: {}", noticeKafkaMessage);
    noticeKafkaService.publish(noticeKafkaMessage, noticeKafkaMessage.newsId());
    return noticeMapper.convert(noticeKafkaMessage);
  }

  @CachePut(value = "notices", key = "#result.id()")
  public NoticeResponseDTO updateNotice(NoticeRequestDTO requestDTO) {
    newsRepository.findById(requestDTO.newsId())
        .orElseThrow(() -> new NotFoundException("News not found"));

    return restTemplate.exchange(
        "%s%s/notices".formatted(discussionProperties.getAddress(), discussionProperties.getBaseApiPath()),
        HttpMethod.PUT,
        new HttpEntity<>(new NoticeRequestDTO(requestDTO.id(), requestDTO.newsId(), requestDTO.content(), EntityStatus.PENDING, STUB_COUNTRY)),
        NoticeResponseDTO.class
    ).getBody();
  }

  @CacheEvict(value = "notices", key = "#id")
  public void deleteNotice(Long id) {
    restTemplate.exchange(
        "%s%s/notices/%d".formatted(discussionProperties.getAddress(), discussionProperties.getBaseApiPath(), id),
        HttpMethod.DELETE,
        HttpEntity.EMPTY,
        Void.class
    );
  }
}

