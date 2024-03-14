package rs.edu.raf.userservice.services;

import rs.edu.raf.userservice.domains.dto.user.CreateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UpdateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UserDto;

import java.util.List;

public interface UserServiceInterface {

    UserDto getUserById(Long id);

    UserDto addUser(CreateUserDto createUserDto);

    UserDto deactivateUser(Long id);

    UserDto updateUser(UpdateUserDto user, Long id);

    List<UserDto> getUsers();

    UserDto getUserByEmail(String email);

    UserDto getUserByMobileNumber(String mobileNumber);

    UserDto getUserByJmbg(String jmbg);

    List<UserDto> search(String firstName, String lastName, String email);
}
