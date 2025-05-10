package by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto;

import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.enums.ReactionState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactionDTO{
    private Long articleId;
    private String country;
    private String content;
    private ReactionState reactionState;
}
