package by.kardychka.Publisher.services;


import by.kardychka.Publisher.DTOs.Requests.NewsRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.NewsResponseDTO;
import by.kardychka.Publisher.models.News;
import by.kardychka.Publisher.models.Sticker;
import by.kardychka.Publisher.models.Creator;
import by.kardychka.Publisher.repositories.NewssRepository;
import by.kardychka.Publisher.repositories.StickersRepository;
import by.kardychka.Publisher.repositories.CreatorsRepository;
import by.kardychka.Publisher.utils.exceptions.NotFoundException;
import by.kardychka.Publisher.utils.mappers.NewssMapper;
import by.kardychka.Publisher.utils.mappers.StickersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class NewssService {
    private final NewssRepository newssRepository;
    private final CreatorsRepository creatorsRepository;
    private final NewssMapper newssMapper;
    private final StickersMapper stickersMapper;
    private StickersRepository stickersRepository;

    @Autowired
    public NewssService(NewssRepository newssRepository, CreatorsRepository creatorsRepository,
                         NewssMapper newssMapper, StickersMapper stickersMapper, StickersRepository stickersRepository) {
        this.newssRepository = newssRepository;
        this.creatorsRepository = creatorsRepository;
        this.newssMapper = newssMapper;
        this.stickersMapper = stickersMapper;
        this.stickersRepository = stickersRepository;
    }

    private void setCreator(News news, long creatorId){
        Creator creator = creatorsRepository.findById(creatorId).orElseThrow(() -> new NotFoundException("Creator with such id does not exist"));
        news.setCreator(creator);
    }

    @Transactional
    public NewsResponseDTO save(NewsRequestDTO newsRequestDTO) {
        News news = newssMapper.toNews(newsRequestDTO);
        setCreator(news, newsRequestDTO.getCreatorId());
        if (newsRequestDTO.getStickers().size() > 0)
            saveStickers(news, newsRequestDTO.getStickers().stream().map(Sticker::new).toList());
        news.setCreated(new Date());
        news.setModified(new Date());
        return newssMapper.toNewsResponse(newssRepository.save(news));
    }

    private void saveStickers(News news, List<Sticker> stickers){
/*        Set<String> stickerNames = stickers.stream().map(Sticker::getName).collect(Collectors.toSet());
        List<Sticker> existingStickers = stickersRepository.findByNameIn(stickerNames);


        Set<String> existingStickerNames = existingStickers.stream().map(Sticker::getName).collect(Collectors.toSet());
        List<Sticker> newStickers = stickers.stream()
                .filter(sticker -> !existingStickerNames.contains(sticker.getName()))
                .collect(Collectors.toList());

        if (!newStickers.isEmpty()) {
            stickersRepository.saveAll(newStickers);
        }

        existingStickers.addAll(newStickers);*/

        news.setStickers(stickers);

/*        for (Sticker sticker : existingStickers) {
            sticker.getNewss().add(news);
        }*/
    }


    @Transactional(readOnly = true)
    public List<NewsResponseDTO> findAll() {
        return newssMapper.toNewsResponseList(newssRepository.findAll());
    }

    @Transactional(readOnly = true)
    public NewsResponseDTO findById(long id) {
        return newssMapper.toNewsResponse(
                newssRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("News with such id does not exist")));
    }

    @Transactional
    public void deleteById(long id) {
        if (!newssRepository.existsById(id)) {
            throw new NotFoundException("News not found");
        }
        newssRepository.deleteById(id);
    }

    @Transactional
    public NewsResponseDTO update(NewsRequestDTO newsRequestDTO) {
        News news = newssMapper.toNews(newsRequestDTO);
        News oldNews = newssRepository.findById(news.getId()).orElseThrow(() -> new NotFoundException("Old news not found"));
        Long creatorId = newsRequestDTO.getCreatorId();

        if (creatorId != null) {
            setCreator(news, creatorId);
        }
        news.setCreated(oldNews.getCreated());
        news.setModified(new Date());
        return newssMapper.toNewsResponse(newssRepository.save(news));
    }

    public boolean existsByTitle(String title){
        return newssRepository.existsByTitle(title);
    }

    public boolean existsById(Long id){
        return newssRepository.existsById(id);
    }
}
