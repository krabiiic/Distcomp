package com.example.rv1.mapper;

import com.example.rv1.dto.MarkDTO;
import com.example.rv1.entity.Mark;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:43:27+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MarkMapperImpl implements MarkMapper {

    @Override
    public Mark toMark(MarkDTO markDTO) {
        if ( markDTO == null ) {
            return null;
        }

        Mark mark = new Mark();

        mark.setId( markDTO.getId() );
        mark.setName( markDTO.getName() );

        return mark;
    }

    @Override
    public MarkDTO toMarkDTO(Mark mark) {
        if ( mark == null ) {
            return null;
        }

        MarkDTO markDTO = new MarkDTO();

        markDTO.setId( mark.getId() );
        markDTO.setName( mark.getName() );

        return markDTO;
    }

    @Override
    public List<MarkDTO> toMarkDTOList(List<Mark> marks) {
        if ( marks == null ) {
            return null;
        }

        List<MarkDTO> list = new ArrayList<MarkDTO>( marks.size() );
        for ( Mark mark : marks ) {
            list.add( toMarkDTO( mark ) );
        }

        return list;
    }

    @Override
    public List<Mark> toMarkList(List<MarkDTO> markDTOS) {
        if ( markDTOS == null ) {
            return null;
        }

        List<Mark> list = new ArrayList<Mark>( markDTOS.size() );
        for ( MarkDTO markDTO : markDTOS ) {
            list.add( toMark( markDTO ) );
        }

        return list;
    }
}
