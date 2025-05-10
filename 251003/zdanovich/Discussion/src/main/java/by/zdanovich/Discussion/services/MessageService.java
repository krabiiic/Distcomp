package by.zdanovich.Discussion.services;


import by.zdanovich.Discussion.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Discussion.DTOs.Responses.MessageResponseDTO;
import by.zdanovich.Discussion.DTOs.kafka.InTopicDTO;
import by.zdanovich.Discussion.DTOs.kafka.OutTopicDTO;
import by.zdanovich.Discussion.models.Message;
import by.zdanovich.Discussion.repositories.MessageRepository;
import by.zdanovich.Discussion.utils.mappers.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Value("${message.country}")
    private String country;

    @KafkaListener(topics = "InTopic", groupId = "messages-group")
    @SendTo
    public org.springframework.messaging.Message<OutTopicDTO> handleMessageRequest(@Payload InTopicDTO request,
                                                                                @Header(name = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopic,
                                                                                @Header(name = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationId) {
        MessageRequestDTO messageRequestDTO = request.getMessageRequestDTO();
        String method = request.getMethod();

        OutTopicDTO response;

        try {
            if (method.equals("POST")) {
                handleSave(messageRequestDTO);
                return null;
            } else if (method.equals("GET")) {
                response = messageRequestDTO != null ? handleFindById(messageRequestDTO.getId()) : handleFindAll();
            } else if (method.equals("PUT")) {
                response = handleUpdate(messageRequestDTO);
            } else if (method.equals("DELETE")) {
                response = handleDelete(messageRequestDTO.getId());
            } else {
                response = new OutTopicDTO("Unsupported method: " + method, "DECLINE");
            }
        } catch (Exception ex) {
            response = new OutTopicDTO("Error: " + ex.getMessage(), "DECLINE");
        }

        if (replyTopic != null && correlationId != null) {
            return MessageBuilder.withPayload(response)
                    .setHeader(KafkaHeaders.TOPIC, new String(replyTopic))
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
        } else {
            return null;
        }
    }


    private OutTopicDTO handleSave(MessageRequestDTO dto) {
        Message message = messageMapper.toMessage(dto);
        message.getKey().setId(dto.getId());
        message.getKey().setCountry(country);
        messageRepository.save(message);
        return new OutTopicDTO(messageMapper.toMessageResponse(message), "APPROVE");
    }

    public List<MessageResponseDTO> findAll() {
        return messageMapper.toMessageResponseList(messageRepository.findAll());
    }

    private OutTopicDTO handleFindAll() {
        List<MessageResponseDTO> messageResponseDTOList = findAll();
        return new OutTopicDTO(messageResponseDTOList, "APPROVE");
    }

    public MessageResponseDTO findById(Long id) {
        return messageMapper.toMessageResponse(messageRepository.findByCountryAndId(country, id)
                .orElseThrow(() -> new RuntimeException("Message not found")));
    }

    private OutTopicDTO handleFindById(Long id) {
        try {
            return new OutTopicDTO(findById(id), "APPROVE");
        } catch (RuntimeException ex) {
            return new OutTopicDTO(ex.getMessage(), "DECLINE");
        }


    }

    private OutTopicDTO handleUpdate(MessageRequestDTO dto) {
        Message message = messageMapper.toMessage(dto);
        message.getKey().setId(dto.getId());
        message.getKey().setCountry(country);
        messageRepository.save(message);
        return new OutTopicDTO(messageMapper.toMessageResponse(message), "APPROVE");
    }

    private OutTopicDTO handleDelete(Long id) {
        Optional<Message> optionalNote = messageRepository.findByCountryAndId(country, id);

        if (optionalNote.isEmpty()) {
            return new OutTopicDTO("Message not found", "DECLINE");
        }

        Message message = optionalNote.get();
        messageRepository.delete(message);
        return new OutTopicDTO(messageMapper.toMessageResponse(message), "APPROVE");
    }

    public MessageResponseDTO update(MessageRequestDTO messageRequestDTO) {
        Message message = messageMapper.toMessage(messageRequestDTO);
        message.getKey().setId(messageRequestDTO.getId());
        message.getKey().setCountry(country);
        return messageMapper.toMessageResponse(messageRepository.save(message));
    }
}
