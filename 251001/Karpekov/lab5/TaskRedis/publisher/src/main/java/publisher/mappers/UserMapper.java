package publisher.mappers;
import publisher.modelDTOs.UserRequestTo;
import publisher.modelDTOs.UserResponseTo;
import publisher.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserRequestTo MapUserToRequestDTO(User user);
    UserResponseTo MapUserToResponseDTO(User user);

    User MapResponseDTOToUser(UserResponseTo userResponseTo);
    User MapRequestDTOToUser(UserRequestTo userRequestTo);
}