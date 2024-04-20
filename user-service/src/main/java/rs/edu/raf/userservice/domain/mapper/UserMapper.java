package rs.edu.raf.userservice.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domain.dto.user.CreateUserDto;
import rs.edu.raf.userservice.domain.dto.user.IsUserActiveDTO;
import rs.edu.raf.userservice.domain.dto.user.UpdateUserDto;
import rs.edu.raf.userservice.domain.dto.user.UserDto;
import rs.edu.raf.userservice.domain.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    IsUserActiveDTO userToIsAUserActiveDTO(User user);

    User userCreateDtoToUser(CreateUserDto userCreateDto);

    void updateUserFromUserUpdateDto(@MappingTarget User user, UpdateUserDto userUpdateDto);
}
