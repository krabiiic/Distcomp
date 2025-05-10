package by.andrewbesedin.distcomp.dto;

import by.andrewbesedin.distcomp.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    TagResponseTo out(Tag editor);

    Tag in(TagRequestTo editor);
}
