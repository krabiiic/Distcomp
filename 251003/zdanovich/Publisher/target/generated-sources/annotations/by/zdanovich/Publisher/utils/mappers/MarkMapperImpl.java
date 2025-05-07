package by.zdanovich.Publisher.utils.mappers;

import by.zdanovich.Publisher.DTOs.Requests.MarkRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MarkResponseDTO;
import by.zdanovich.Publisher.models.Mark;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-07T21:47:14+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MarkMapperImpl implements MarkMapper {

    @Override
    public MarkResponseDTO toMarkResponse(Mark mark) {
        if ( mark == null ) {
            return null;
        }

        MarkResponseDTO markResponseDTO = new MarkResponseDTO();

        markResponseDTO.setId( mark.getId() );
        markResponseDTO.setName( mark.getName() );

        return markResponseDTO;
    }

    @Override
    public List<MarkResponseDTO> toMarkResponseList(List<Mark> marks) {
        if ( marks == null ) {
            return null;
        }

        List<MarkResponseDTO> list = new ArrayList<MarkResponseDTO>( marks.size() );
        for ( Mark mark : marks ) {
            list.add( toMarkResponse( mark ) );
        }

        return list;
    }

    @Override
    public Mark toMark(MarkRequestDTO markRequestDTO) {
        if ( markRequestDTO == null ) {
            return null;
        }

        Mark mark = new Mark();

        if ( markRequestDTO.getId() != null ) {
            mark.setId( markRequestDTO.getId() );
        }
        mark.setName( markRequestDTO.getName() );

        return mark;
    }

    @Override
    public List<Mark> toMarkList(List<MarkRequestDTO> markRequestDTOList) {
        if ( markRequestDTOList == null ) {
            return null;
        }

        List<Mark> list = new ArrayList<Mark>( markRequestDTOList.size() );
        for ( MarkRequestDTO markRequestDTO : markRequestDTOList ) {
            list.add( toMark( markRequestDTO ) );
        }

        return list;
    }
}
