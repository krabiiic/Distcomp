package org.ex.distributed_computing.dto.response;

import java.time.LocalDateTime;

public record TweetResponseDTO(Long id, Long writerId, String title, String content,
                               LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {}
