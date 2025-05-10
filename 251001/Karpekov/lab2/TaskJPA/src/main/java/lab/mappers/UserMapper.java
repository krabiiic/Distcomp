package lab.mappers;
import lab.modelDTOs.UserRequestTo;
import lab.modelDTOs.UserResponseTo;
import lab.models.User;
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