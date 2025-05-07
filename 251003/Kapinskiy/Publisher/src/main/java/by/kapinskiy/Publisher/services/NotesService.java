package by.kapinskiy.Publisher.services;

import by.kapinskiy.Publisher.DTOs.Requests.NoteRequestDTO;
import by.kapinskiy.Publisher.DTOs.Responses.NoteResponseDTO;
import by.kapinskiy.Publisher.DTOs.kafka.InTopicDTO;
import by.kapinskiy.Publisher.DTOs.kafka.OutTopicDTO;
import by.kapinskiy.Publisher.utils.exceptions.NotFoundException;
import by.kapinskiy.Publisher.utils.mappers.NotesMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.KafkaReplyTimeoutException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class NotesService {
    private final ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate;
    private final KafkaTemplate<String, InTopicDTO> kafkaTemplate;
    private final NotesMapper notesMapper;
    private static final String IN_TOPIC = "InTopic";

    public NoteResponseDTO createNote(NoteRequestDTO requestDTO) {
        Long generatedId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        requestDTO.setId(generatedId);

        InTopicDTO inDto = new InTopicDTO(
                "POST",
                requestDTO,
                "PENDING"
        );

        kafkaTemplate.send(IN_TOPIC, generatedId.toString(), inDto);

        return notesMapper.toNoteResponse(requestDTO);
    }

    public List<NoteResponseDTO> getAllNotes() {
        InTopicDTO request = new InTopicDTO(
                "GET",
                null,
                "PENDING"
        );
        String id = UUID.randomUUID().toString();
        OutTopicDTO response = sendAndReceiveInternal(request, id);
        return response.getNoteResponsesListDTO();
    }

    @Cacheable(value = "notes", key = "#id")
    public NoteResponseDTO getNoteById(Long id) {
        InTopicDTO request = new InTopicDTO(
                "GET",
                new NoteRequestDTO(id),
                "PENDING"
        );
        OutTopicDTO response = sendAndReceiveInternal(request, id.toString());
        if (response.getError() != null && !response.getError().isEmpty()) {
            throw new NotFoundException(response.getError());
        }
        return response.getNoteResponseDTO();
    }

    @CacheEvict(value = "notes", key = "#noteRequestDTO.id")
    public NoteResponseDTO processNoteRequest(String httpMethod, NoteRequestDTO noteRequestDTO) {
        InTopicDTO request = new InTopicDTO(
                httpMethod,
                noteRequestDTO,
                "PENDING"
        );

        OutTopicDTO response = sendAndReceiveInternal(request, noteRequestDTO.getId().toString());
        if (response.getError() != null && !response.getError().contains("Not found")) {
            throw new NotFoundException(response.getError());
        }
        return response.getNoteResponseDTO();

    }


    private OutTopicDTO sendAndReceiveInternal(InTopicDTO request, String correlationId) {
        ProducerRecord<String, InTopicDTO> record = new ProducerRecord<>(IN_TOPIC, correlationId, request);
        RequestReplyFuture<String, InTopicDTO, OutTopicDTO> future = replyingKafkaTemplate.sendAndReceive(record);
        try {
            ConsumerRecord<String, OutTopicDTO> response = future.get(10, TimeUnit.SECONDS);
            return response.value();
        } catch (TimeoutException e) {
            throw new KafkaReplyTimeoutException("Timeout waiting for note data");
        } catch (InterruptedException | ExecutionException e) {
            throw new KafkaException("Error processing Kafka request", e);
        }
    }

}