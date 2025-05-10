package com.rita.publisher.mapper;

import com.rita.publisher.model.Sticker;
import com.rita.publisher.model.Tweet;
import com.rita.publisher.model.Writer;
import com.rita.publisher.repository.StickerRepository;
import com.rita.publisher.repository.TweetRepository;
import com.rita.publisher.repository.WriterRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Named("MapperUtil")
@Component
@RequiredArgsConstructor
public class MapperUtil {
    @Autowired
    WriterRepository writerRepository;
    @Autowired
    TweetRepository tweetRepository;
    @Autowired
    StickerRepository stickerRepository;

    @Named("getStickers")
    public Set<Sticker> getStickers(Set<String> ids){
        if(ids!=null&& ids.size()>0) {
            Set<Long> res=new HashSet<>();

            ids.stream().forEach(x->res.add(stickerRepository.save(new Sticker(null,x,null)).getId()));
            return res.stream().map(x -> stickerRepository.findById(x).orElseThrow(() -> new EntityNotFoundException("Sticker not found with id: " + x))).collect(Collectors.toSet());

        }
        else return new HashSet<>();
    }

    @Named("getWriter")
    public Writer getWriter(Long id){
        return writerRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Writer not found with id: "+id));
    }

    @Named("getWriterId")
    public Long getWriterId(Writer writer){
        return writer.getId();
    }
    @Named("getTweet")
    public Tweet getTweet(Long id){
        return tweetRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Tweet not found with id: "+id));
    }

    @Named("getTweetId")
    public Long getTweetId(Tweet tweet){
        return tweet.getId();
    }
//
//    @Named("getMessages")
//    public Set<TweetFullDTO.TweetFullDTOMessage> getMessages(Long id){
//        Set<TweetFullDTO.TweetFullDTOMessage> res=new HashSet<>();
//        Set<Message> set=messageRepository.getMessagesByTweetId(id);
//        set.stream().forEach(x->res.add(new TweetFullDTO.TweetFullDTOMessage(x.getId(),x.getContent())));
//        return res;
//    }
//    @Named("getStickers")
//    public Set<TweetFullDTO.TweetFullDTOSticker> getStickers(Long id){
//        Set<TweetFullDTO.TweetFullDTOSticker> res=new HashSet<>();
//        Set<Sticker> set=stickerRepository.getStickersByTweetId(id);
//        set.stream().forEach(x->res.add(new TweetFullDTO.TweetFullDTOSticker(x.getId(),x.getName())));
//        return res;
//    }
//    @Named("getWriter")
//    public TweetFullDTO.TweetFullDTOWriter getWriter(Long id){
//        Writer writer=writerRepository.findById(tweetRepository.getWriterId(id)).orElseThrow(()->new EntityNotFoundException("Writer not found for tweet with id: "+id));
//        return new TweetFullDTO.TweetFullDTOWriter(writer.getId(),writer.getLogin());
//    }
//    @Named("getMessageIds")
//    public Set<Long> getMessageIds(Long id){
//        return messageRepository.getMessageIdsByTweetId(id);
//    }
//
//    @Named("getStickerIds")
//    public Set<Long> getStickerIds(Long id){
//        return tweetRepository.getStickerIds(id);
//    }
//
//
//    @Named("getStickerIdsEmpty")
//    public static Set<Long> getStickerIdsEmpty(){
//        return new HashSet<>();
//    }
//
//    @Named("getTweetIds")
//    public Set<Long> getTweetIds(Long id){
//        return stickerRepository.getTweetIds(id);
//    }
//
//    @Named("getTweetIdsEmpty")
//    public static Set<Long> getTweetIdsEmpty(){
//        return new HashSet<>();
//    }
//
//    @Named("getWriterFromWriterId")
//    Writer writerIdToWriter(Long id) {
//        return writerRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Writer not found with id: "+id));
//    }
//    @Named("getWriterIdFromWriter")
//    Long writerToWriterId(Writer writer) {
//            if(writer==null)
//                throw new EntityNotFoundException("Writer not found");
//        return writer.getId();
//    }
}
