package by.kardychka.Publisher.services;


import by.kardychka.Publisher.DTOs.Requests.StickerRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.StickerResponseDTO;
import by.kardychka.Publisher.models.News;
import by.kardychka.Publisher.models.Sticker;
import by.kardychka.Publisher.repositories.NewssRepository;
import by.kardychka.Publisher.repositories.StickersRepository;
import by.kardychka.Publisher.utils.exceptions.NotFoundException;
import by.kardychka.Publisher.utils.mappers.StickersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StickersService {
    private final StickersRepository stickersRepository;
    private final NewssRepository newssRepository;
    private final StickersMapper stickersMapper;
    

    @Autowired
    public StickersService(StickersRepository stickersRepository, NewssRepository newssRepository, StickersMapper stickersMapper) {
        this.stickersRepository = stickersRepository;
        this.newssRepository = newssRepository;
        this.stickersMapper = stickersMapper;
    }

    public StickerResponseDTO save(Sticker sticker, long newsId) {
        News news = newssRepository.findById(newsId).orElseThrow(() -> new NotFoundException("News with such id does not exist"));
        news.getStickers().add(sticker);
        sticker.getNewss().add(news);
        return stickersMapper.toStickerResponse(stickersRepository.save(sticker));
    }

    public StickerResponseDTO save(StickerRequestDTO stickerRequestDTO) {
        Sticker sticker = stickersMapper.toSticker(stickerRequestDTO);
        return stickersMapper.toStickerResponse(stickersRepository.save(sticker));
    }
    public List<StickerResponseDTO> findAll() {
        return stickersMapper.toStickerResponseList(stickersRepository.findAll());
    }

    public StickerResponseDTO findById(long id) {
        return stickersMapper.toStickerResponse(stickersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sticker with such id does not exist")));
    }

    public void deleteById(long id) {
        if (!stickersRepository.existsById(id))
            throw new NotFoundException("Sticker with such id not found");
        stickersRepository.deleteById(id);
    }

    public StickerResponseDTO update(StickerRequestDTO stickerRequestDTO) {
        Sticker sticker = stickersMapper.toSticker(stickerRequestDTO);
        return stickersMapper.toStickerResponse(stickersRepository.save(sticker));
    }

    public boolean existsByName(String name){
        return stickersRepository.existsByName(name);
    }

}
