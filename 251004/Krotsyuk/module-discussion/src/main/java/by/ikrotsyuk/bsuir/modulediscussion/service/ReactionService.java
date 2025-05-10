package by.ikrotsyuk.bsuir.modulediscussion.service;

import by.ikrotsyuk.bsuir.modulediscussion.dto.request.ReactionRequestDTO;
import by.ikrotsyuk.bsuir.modulediscussion.dto.response.ReactionResponseDTO;
import by.ikrotsyuk.bsuir.modulediscussion.entity.ReactionEntity;
import by.ikrotsyuk.bsuir.modulediscussion.exception.exceptions.NotFoundException;
import by.ikrotsyuk.bsuir.modulediscussion.mapper.ReactionMapper;
import by.ikrotsyuk.bsuir.modulediscussion.repository.ReactionRepository;
import by.ikrotsyuk.bsuir.modulediscussion.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final IdGenerator idGenerator;

    public List<ReactionResponseDTO> getReactions(){
        List<ReactionEntity> reactionEntityList = reactionRepository.findAll();
        if(reactionEntityList.isEmpty())
            return Collections.emptyList();
        else
            return reactionMapper.toDTOList(reactionEntityList);
    }

    public ReactionResponseDTO getReactionById(Long id){
        Optional<ReactionEntity> optionalReactionEntity = reactionRepository.findById(id);
        return optionalReactionEntity.map(reactionMapper::toDTO).orElseThrow(NotFoundException::new);
    }

    public ReactionResponseDTO addReaction(ReactionRequestDTO reactionRequestDTO){
        ReactionEntity reactionEntity = reactionMapper.toEntity(reactionRequestDTO);
        reactionEntity.setId(idGenerator.generateSequence());
        return reactionMapper.toDTO(reactionRepository.save(reactionEntity));
    }

    public ReactionResponseDTO deleteReaction(Long id){
        Optional<ReactionEntity> optionalReactionEntity = reactionRepository.findById(id);
        if(optionalReactionEntity.isPresent()) {
            reactionRepository.deleteById(id);
            return reactionMapper.toDTO(optionalReactionEntity.get());
        } else
            throw new NotFoundException();
    }

    public ReactionResponseDTO updateReaction(Long id, ReactionRequestDTO reactionRequestDTO){
        ReactionEntity reactionEntity = reactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        reactionEntity.setCountry(reactionRequestDTO.getCountry());
        reactionEntity.setContent(reactionRequestDTO.getContent());
        reactionEntity.setArticleId(reactionRequestDTO.getArticleId());
        reactionRepository.save(reactionEntity);
        return reactionMapper.toDTO(reactionEntity);
    }
}
