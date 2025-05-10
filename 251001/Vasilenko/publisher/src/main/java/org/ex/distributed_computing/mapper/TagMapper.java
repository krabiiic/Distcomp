package org.ex.distributed_computing.mapper;

import java.util.List;
import org.ex.distributed_computing.dto.request.TagRequestDTO;
import org.ex.distributed_computing.dto.response.TagResponseDTO;
import org.ex.distributed_computing.model.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

  Tag toEntity(TagRequestDTO dto);

  TagResponseDTO toDto(Tag tag);

  List<TagResponseDTO> toDtoList(List<Tag> tags);
}

