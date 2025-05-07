package org.ex.distributed_computing.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.distributed_computing.config.DiscussionCommunicationProps;
import org.ex.distributed_computing.dto.request.ReactionRequestDTO;
import org.ex.distributed_computing.dto.response.ReactionResponseDTO;
import org.ex.distributed_computing.exception.NotFoundException;
import org.ex.distributed_computing.kafka.reaction.ReactionKafkaService;
import org.ex.distributed_computing.mapper.ReactionMapper;
import org.ex.distributed_computing.model.EntityStatus;
import org.ex.distributed_computing.repository.ReactionRepository;
import org.ex.distributed_computing.repository.TweetRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionService {

  private static final String STUB_COUNTRY = "US"; // since not supplied

  private final DiscussionCommunicationProps discussionProperties;
  private final ReactionRepository reactionRepository;
  private final TweetRepository tweetRepository;
  private final RestTemplate restTemplate;
  private final ReactionKafkaService reactionKafkaService;
  private final ReactionMapper reactionMapper;

  public List<ReactionResponseDTO> getAllReactions() {
    return restTemplate.exchange(
        "%s%s/reactions".formatted(discussionProperties.getAddress(), discussionProperties.getBaseApiPath()),
        HttpMethod.GET,
        HttpEntity.EMPTY,
        new ParameterizedTypeReference<List<ReactionResponseDTO>>() {
        }
    ).getBody();
  }

  @Cacheable(value = "reactions", key = "#id")
  public ReactionResponseDTO getReactionById(Long id) {
    return restTemplate.getForObject(
        "%s%s/reactions/%d".formatted(discussionProperties.getAddress(), discussionProperties.getBaseApiPath(), id),
        ReactionResponseDTO.class
    );
  }

  @CachePut(value = "reactions", key = "#result.id()")
  public ReactionResponseDTO createReaction(ReactionRequestDTO requestDTO) {
    tweetRepository.findById(requestDTO.tweetId())
        .orElseThrow(() -> new NotFoundException("Tweet not found"));

    Long nextReactionId = reactionRepository.nextId();
    var noticeKafkaMessage = new ReactionRequestDTO(nextReactionId, requestDTO.tweetId(), requestDTO.content(), EntityStatus.PENDING, STUB_COUNTRY);

    log.info("Sending new notice message: {}", noticeKafkaMessage);
    reactionKafkaService.publish(noticeKafkaMessage, noticeKafkaMessage.tweetId());
    return reactionMapper.convert(noticeKafkaMessage);
  }

  @CachePut(value = "reactions", key = "#result.id()")
  public ReactionResponseDTO updateReaction(ReactionRequestDTO requestDTO) {
    tweetRepository.findById(requestDTO.tweetId())
        .orElseThrow(() -> new NotFoundException("Tweet not found"));

    return restTemplate.exchange(
        "%s%s/reactions".formatted(discussionProperties.getAddress(), discussionProperties.getBaseApiPath()),
        HttpMethod.PUT,
        new HttpEntity<>(new ReactionRequestDTO(requestDTO.id(), requestDTO.tweetId(), requestDTO.content(), EntityStatus.PENDING, STUB_COUNTRY)),
        ReactionResponseDTO.class
    ).getBody();
  }

  @CacheEvict(value = "reactions", key = "#id")
  public void deleteReaction(Long id) {
    restTemplate.exchange(
        "%s%s/reactions/%d".formatted(discussionProperties.getAddress(), discussionProperties.getBaseApiPath(), id),
        HttpMethod.DELETE,
        HttpEntity.EMPTY,
        Void.class
    );
  }
}

