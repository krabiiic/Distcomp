package com.dc.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageKafka implements Serializable {

    private Long id;

    private Long newsId;

    private String content;

    private String status;

    private String purpose;

    private String correlationId;
}
