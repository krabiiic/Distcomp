package com.rita.publisher.service;


import com.rita.publisher.dto.*;
import com.rita.publisher.exception.GlobalException;
import com.rita.publisher.mapper.TweetMapper;
import com.rita.publisher.model.Sticker;
import com.rita.publisher.model.Tweet;
import com.rita.publisher.repository.TweetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TweetService {
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private TweetMapper tweetMapper;
    @Autowired
    private StickerService stickerService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    private  WriterService writerService;


    public TweetDTO getTweet(Long id){
        return tweetMapper.toTweetDTO(tweetRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Writer not found with id: "+id)));
    }

    public List<TweetDTO>  getAllTweets(){
        List<TweetDTO> list=new ArrayList<>();
        tweetRepository.findAll().forEach(x->list.add(tweetMapper.toTweetDTO(x)));
        return list;
    }
    public Page<TweetDTO> getAllTweets(Pageable pageable){
        List<TweetDTO> tweets = tweetRepository.findAll(pageable).stream().map(x->tweetMapper.toTweetDTO(x)).toList();
       long total=tweetRepository.count();
        return new PageImpl<>(tweets,pageable,total);
    }
    public TweetDTO createTweet(TweetCreateDTO tweetDTO){
        if(tweetDTO.title().length()<2||tweetDTO.title().length()>64||tweetDTO.content().length()<4||tweetDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        Tweet tweet=tweetMapper.toTweet(tweetDTO);

        try {
            return tweetMapper.toTweetDTO(tweetRepository.save(tweet));
        }catch (RuntimeException e){
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN,"DB");
        }
    }

    public TweetDTO updateTweet(TweetUpdateDTO tweetDTO){
        if(tweetDTO.title().length()<2||tweetDTO.title().length()>64||tweetDTO.content().length()<4||tweetDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        Tweet tweet=tweetMapper.toTweet(tweetDTO);
       Tweet old= tweetRepository.findById(tweet.getId()).orElseThrow(()->new EntityNotFoundException("Tweet not found with id: "+tweet.getId()));
       tweet.setCreatedTime(old.getCreatedTime());
       tweet.setModifiedTime(LocalDateTime.now());
       return tweetMapper.toTweetDTO(tweetRepository.save(tweet));
    }

//    public TweetDTO patchTweet(TweetUpdateDTO tweetDTO){
//        Tweet old= tweetRepository.findById(tweetDTO.id()).orElseThrow(()->new EntityNotFoundException("Tweet not found with id: "+tweetDTO.id()));
//        tweetMapper.toPatchedTweet(old,tweetDTO);
//        old.setModifiedTime(LocalDateTime.now());
//        return tweetMapper.toTweetDTO(tweetRepository.save(old));
//    }

    public void deleteTweet(Long id){

        if(tweetRepository.existsById(id)){
            Tweet tweet=tweetRepository.findById(id).orElseThrow();

            List<Sticker> stickers = stickerService.getStickers();//tweetRepository.findStickersByTweetId(tweet.getId());
            if (stickers != null && !stickers.isEmpty()) {

                List<Long> stickerIdsToDelete = new ArrayList<>();
                for (Sticker sticker : stickers) {

                    stickerIdsToDelete.add(sticker.getId());
                }
                for (Long id1 : stickerIdsToDelete) {
                   System.out.println(stickerService.getSticker(id1).name());
                    stickerService.deleteSticker(id1);
                }
            }
        }
        tweetRepository.deleteById(id);
    }

//    public void deleteTweet(Long id) {
//        if (tweetRepository.existsById(id)) {
//            Tweet tweet = tweetRepository.findById(id).orElseThrow();
//            Set<Sticker> stickers = tweet.getStickers();
//            if (stickers != null && !stickers.isEmpty()) {
//                for (Sticker sticker : new HashSet<>(stickers)) {
//                    entityManager.remove(entityManager.contains(sticker) ? sticker : entityManager.merge(sticker));
//                }
//            }
//            tweetRepository.deleteById(id);
//        }
//    }
    public Long getWriterId(Long id){
        return tweetRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Tweet not found with id: "+id)).getWriter().getId();
    }


    public static <T> List<T> convertIterableToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T item : iterable) {
            list.add(item);
        }
        return list;
    }

}
