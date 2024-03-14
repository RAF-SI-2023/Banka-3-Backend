package rs.edu.raf.userservice.services;

import rs.edu.raf.userservice.domains.dto.CreateUserDto;
import rs.edu.raf.userservice.domains.dto.UpdateUserDto;
import rs.edu.raf.userservice.domains.dto.UserDto;
import rs.edu.raf.userservice.domains.model.Role;
import rs.edu.raf.userservice.domains.model.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    UserDto getUserById(Long id);
    UserDto addUser(CreateUserDto createUserDto);
    UserDto deactivateUser(Long id);
    UserDto updateUser(UpdateUserDto user, Long id);
    List<UserDto> getUsers();
    UserDto getUserByEmail(String email);
    UserDto getUserByMobileNumber(String mobileNumber);
    UserDto getUserByJmbg(String jmbg);


}
