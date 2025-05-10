package com.rita.publisher.service;

import com.rita.publisher.dto.WriterCreateDTO;
import com.rita.publisher.dto.WriterDTO;
import com.rita.publisher.dto.WriterUpdateDTO;
import com.rita.publisher.exception.GlobalException;
import com.rita.publisher.mapper.WriterMapper;
import com.rita.publisher.model.Writer;
import com.rita.publisher.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WriterService {
    @Autowired
    WriterRepository writerRepository;
    @Autowired
    WriterMapper writerMapper;
    @Autowired
    TweetService tweetService;

    public WriterDTO getWriter(Long id){
        return writerMapper.toWriterDTO(writerRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Writer not found with id: "+id)));
    }
    public List<WriterDTO> getAllWriters(){
        List<WriterDTO> list=new ArrayList<>();
        writerRepository.findAll().forEach(x->list.add(writerMapper.toWriterDTO(x)));
        return list;
    }
    public Page<WriterDTO> getAllWriters(Pageable pageable){
        List<WriterDTO> writers = writerRepository.findAll(pageable).stream().map(x->writerMapper.toWriterDTO(x)).toList();
        long total= writerRepository.count();
        return new PageImpl<>(writers,pageable,total);
    }
    public WriterDTO createWriter(WriterCreateDTO writerDTO){
        if(writerDTO.login().length()<2||writerDTO.login().length()>64||writerDTO.password().length()<8||writerDTO.password().length()>128||writerDTO.firstname().length()<2||writerDTO.firstname().length()>64||writerDTO.lastname().length()<2||writerDTO.lastname().length()>64) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        try {
            WriterDTO res = writerMapper.toWriterDTO(writerRepository.save(writerMapper.toWriter(writerDTO)));
        return res;
        }catch (RuntimeException e){
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN,"DB");
        }
    }

    public WriterDTO updateWriter(WriterUpdateDTO writerDTO){
        if(writerDTO.login().length()<2||writerDTO.login().length()>64||writerDTO.password().length()<8||writerDTO.password().length()>128||writerDTO.firstname().length()<2||writerDTO.firstname().length()>64||writerDTO.lastname().length()<2||writerDTO.lastname().length()>64) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        Writer writer=writerMapper.toWriter(writerDTO);
        Writer old= writerRepository.findById(writer.getId()).orElseThrow(()->new EntityNotFoundException("Writer not found with id: "+writer.getId()));
        writer.setTweets(old.getTweets());
        return writerMapper.toWriterDTO(writerRepository.save(writer));
    }

//    public WriterDTO patchWriter(WriterUpdateDTO writerDTO){
//        Writer writer=writerRepository.findById(writerDTO.id()).orElseThrow(()->new EntityNotFoundException("Writer not found with id: "+writerDTO.id()));
//
//        writerMapper.toPatchedWriter(writer,writerDTO);
//        return writerMapper.toWriterDTO(writerRepository.save(writer));
//    }
    public void deleteWriter(Long id){
        writerRepository.deleteById(id);
    }

    public WriterDTO getWriterByTweetId(Long tweetId){
        Long writerId=tweetService.getWriterId(tweetId);
        return writerMapper.toWriterDTO(writerRepository.findById(writerId).orElseThrow(()->new EntityNotFoundException("Writer not found with id: "+writerId)));
    }
    public String getWriterLoginByWriterId(Long writerId){
        return writerRepository.findById(writerId).orElseThrow(()->new EntityNotFoundException("Writer not found with id: "+writerId)).getLogin();

    }
}
