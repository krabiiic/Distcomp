package org.ex.distributed_computing.mapper;

import java.util.List;
import org.ex.distributed_computing.dto.request.WriterRequestDTO;
import org.ex.distributed_computing.dto.response.WriterResponseDTO;
import org.ex.distributed_computing.model.Writer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface WriterMapper {

  Writer toEntity(WriterRequestDTO dto);

  @Mapping(target = "password", ignore = true)
  WriterResponseDTO toDto(Writer author);

  List<WriterResponseDTO> toDtoList(List<Writer> authors);
}
