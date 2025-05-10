package by.kapinskiy.Discussion.services;


import by.kapinskiy.Discussion.DTOs.Requests.NoteRequestDTO;
import by.kapinskiy.Discussion.DTOs.Responses.NoteResponseDTO;
import by.kapinskiy.Discussion.DTOs.kafka.InTopicDTO;
import by.kapinskiy.Discussion.DTOs.kafka.OutTopicDTO;
import by.kapinskiy.Discussion.models.Note;
import by.kapinskiy.Discussion.repositories.NotesRepository;
import by.kapinskiy.Discussion.utils.mappers.NotesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotesService {

    private final NotesRepository notesRepository;
    private final NotesMapper notesMapper;

    @Value("${note.country}")
    private String country;

    @KafkaListener(topics = "InTopic", groupId = "notes-group")
    @SendTo
    public Message<OutTopicDTO> handleNoteRequest(@Payload InTopicDTO request,
                                                  @Header(name = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopic,
                                                  @Header(name = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationId) {
        NoteRequestDTO noteRequestDTO = request.getNoteRequestDTO();
        String method = request.getMethod();

        OutTopicDTO response;

        try {
            if (method.equals("POST")) {
                handleSave(noteRequestDTO);
                return null;
            } else if (method.equals("GET")) {
                response = noteRequestDTO != null ? handleFindById(noteRequestDTO.getId()) : handleFindAll();
            } else if (method.equals("PUT")) {
                response = handleUpdate(noteRequestDTO);
            } else if (method.equals("DELETE")) {
                response = handleDelete(noteRequestDTO.getId());
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


    private OutTopicDTO handleSave(NoteRequestDTO dto) {
        Note note = notesMapper.toNote(dto);
        note.getKey().setId(dto.getId());
        note.getKey().setCountry(country);
        notesRepository.save(note);
        return new OutTopicDTO(notesMapper.toNoteResponse(note), "APPROVE");
    }

    public List<NoteResponseDTO> findAll() {
        return notesMapper.toNoteResponseList(notesRepository.findAll());
    }

    private OutTopicDTO handleFindAll() {
        List<NoteResponseDTO> noteResponseDTOList = findAll();
        return new OutTopicDTO(noteResponseDTOList, "APPROVE");
    }

    public NoteResponseDTO findById(Long id) {
        return notesMapper.toNoteResponse(notesRepository.findByCountryAndId(country, id)
                .orElseThrow(() -> new RuntimeException("Note not found")));
    }

    private OutTopicDTO handleFindById(Long id) {
        try {
            return new OutTopicDTO(findById(id), "APPROVE");
        } catch (RuntimeException ex) {
            return new OutTopicDTO(ex.getMessage(), "DECLINE");
        }


    }

    private OutTopicDTO handleUpdate(NoteRequestDTO dto) {
        Note note = notesMapper.toNote(dto);
        note.getKey().setId(dto.getId());
        note.getKey().setCountry(country);
        notesRepository.save(note);
        return new OutTopicDTO(notesMapper.toNoteResponse(note), "APPROVE");
    }

    private OutTopicDTO handleDelete(Long id) {
        Optional<Note> optionalNote = notesRepository.findByCountryAndId(country, id);

        if (optionalNote.isEmpty()) {
            return new OutTopicDTO("Note not found", "DECLINE");
        }

        Note note = optionalNote.get();
        notesRepository.delete(note);
        return new OutTopicDTO(notesMapper.toNoteResponse(note), "APPROVE");
    }

    public NoteResponseDTO update(NoteRequestDTO noteRequestDTO) {
        Note note = notesMapper.toNote(noteRequestDTO);
        note.getKey().setId(noteRequestDTO.getId());
        note.getKey().setCountry(country);
        return notesMapper.toNoteResponse(notesRepository.save(note));
    }
}
