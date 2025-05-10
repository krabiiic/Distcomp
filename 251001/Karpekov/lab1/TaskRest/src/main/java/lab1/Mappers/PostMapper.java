package lab1.Mappers;
import lab1.modelDTOs.PostRequestTo;
import lab1.modelDTOs.PostResponseTo;
import lab1.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );

    PostRequestTo MapPostToRequestDTO(Post post);
    PostResponseTo MapPostToResponseDTO(Post post);

    Post MapResponseDTOToPost(PostResponseTo postResponseTo);
    Post MapRequestDTOToPost(PostRequestTo postRequestTo);
}