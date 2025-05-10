package by.zdanovich.Publisher.utils.mappers;

import by.zdanovich.Publisher.DTOs.Requests.WriterRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.WriterResponseDTO;
import by.zdanovich.Publisher.models.Writer;
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
public class WriterMapperImpl implements WriterMapper {

    @Override
    public WriterResponseDTO toWriterResponse(Writer writer) {
        if ( writer == null ) {
            return null;
        }

        WriterResponseDTO writerResponseDTO = new WriterResponseDTO();

        writerResponseDTO.setId( writer.getId() );
        writerResponseDTO.setLogin( writer.getLogin() );
        writerResponseDTO.setPassword( writer.getPassword() );
        writerResponseDTO.setFirstname( writer.getFirstname() );
        writerResponseDTO.setLastname( writer.getLastname() );

        return writerResponseDTO;
    }

    @Override
    public List<WriterResponseDTO> toWriterResponseList(List<Writer> writers) {
        if ( writers == null ) {
            return null;
        }

        List<WriterResponseDTO> list = new ArrayList<WriterResponseDTO>( writers.size() );
        for ( Writer writer : writers ) {
            list.add( toWriterResponse( writer ) );
        }

        return list;
    }

    @Override
    public Writer toWriter(WriterRequestDTO userRequestDTO) {
        if ( userRequestDTO == null ) {
            return null;
        }

        Writer writer = new Writer();

        if ( userRequestDTO.getId() != null ) {
            writer.setId( userRequestDTO.getId() );
        }
        writer.setLogin( userRequestDTO.getLogin() );
        writer.setPassword( userRequestDTO.getPassword() );
        writer.setFirstname( userRequestDTO.getFirstname() );
        writer.setLastname( userRequestDTO.getLastname() );

        return writer;
    }
}
