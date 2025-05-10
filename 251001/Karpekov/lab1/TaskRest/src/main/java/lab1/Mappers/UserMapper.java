package lab1.Mappers;
import lab1.modelDTOs.UserRequestTo;
import lab1.modelDTOs.UserResponseTo;
import lab1.models.User;
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