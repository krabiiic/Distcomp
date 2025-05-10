package com.rita.publisher.mapper;

import com.rita.publisher.dto.WriterCreateDTO;
import com.rita.publisher.dto.WriterDTO;
import com.rita.publisher.dto.WriterUpdateDTO;
import com.rita.publisher.model.Writer;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WriterMapper {
    @Mapping(target = "tweets", ignore = true)
    Writer toWriter(WriterUpdateDTO writerUpdateDTO);
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "tweets", ignore = true)
    Writer toWriter(WriterCreateDTO writerCreateDTO);
    WriterDTO toWriterDTO(Writer writer);

//    @Mapping(target = "id",source = "id")
//    @Mapping(target = "login", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "password", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "firstname", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "lastname", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "tweets", ignore = true)
//    void toPatchedWriter(@MappingTarget Writer writer,
//                         WriterUpdateDTO patchWriterRequest);
}
