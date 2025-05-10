package org.ex.distributed_computing.dto.response;

import java.io.Serializable;

public record NoticeResponseDTO(Long id, Long newsId, String content, String country) implements Serializable {}

