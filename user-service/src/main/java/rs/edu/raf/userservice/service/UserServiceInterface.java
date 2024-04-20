package rs.edu.raf.userservice.service;

import rs.edu.raf.userservice.domain.dto.user.CreateUserDto;
import rs.edu.raf.userservice.domain.dto.user.UpdateUserDto;
import rs.edu.raf.userservice.domain.dto.user.UserDto;

import java.util.List;

public interface UserServiceInterface {

    UserDto getUserById(Long id);

    UserDto addUser(CreateUserDto createUserDto);

    UserDto deactivateUser(Long id);

    UserDto updateUser(UpdateUserDto user, Long id);

    List<UserDto> getUsers();

    UserDto getUserByEmail(String email);

    List<UserDto> search(String firstName, String lastName, String email);
}
