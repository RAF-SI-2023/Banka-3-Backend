package rs.edu.raf.userservice.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domain.dto.user.IsUserActiveDto;
import rs.edu.raf.userservice.domain.dto.user.UserDto;
import rs.edu.raf.userservice.domain.dto.user.UserEmailDto;
import rs.edu.raf.userservice.domain.dto.user.UserPostPutDto;
import rs.edu.raf.userservice.domain.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    IsUserActiveDto userToIsAUserActiveDTO(User user);

    User userCreateDtoToUser(UserPostPutDto userPostPutDto);

    void updateUserFromUserUpdateDto(@MappingTarget User user, UserPostPutDto userUpdateDto);

    UserEmailDto userEmailDtoFromUser(User user);
}
