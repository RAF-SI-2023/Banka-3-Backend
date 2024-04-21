package rs.edu.raf.userservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.edu.raf.userservice.domain.dto.user.*;
import rs.edu.raf.userservice.domain.exception.ForbiddenException;
import rs.edu.raf.userservice.domain.exception.NotFoundException;
import rs.edu.raf.userservice.domain.mapper.UserMapper;
import rs.edu.raf.userservice.domain.model.User;
import rs.edu.raf.userservice.repository.UserRepository;
import rs.edu.raf.userservice.util.client.EmailServiceClient;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
    private final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private final Pattern jmbgPattern = Pattern.compile("[0-9]{13}");
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailServiceClient emailServiceClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not " +
                "found"));

        if (user == null) {
            throw new UsernameNotFoundException("User with the email: " + email + " not found");
        }
        if (!user.isActive()) {
            throw new ForbiddenException("user not active");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserMapper.INSTANCE::userToUserDto).orElseThrow(() -> new NotFoundException("user with" + id + " not found"));
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NotFoundException("not found users");
        }
        return users.stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toList());
    }

    public UserDto addUser(UserPostPutDto userPostPutDto) {
        if (!emailPattern.matcher(userPostPutDto.getEmail()).matches()) {
            throw new ValidationException("invalid email");
        }
        if (!jmbgPattern.matcher(userPostPutDto.getJmbg()).matches()) {
            throw new ValidationException("invalid jmbg");
        }
        User user = UserMapper.INSTANCE.userCreateDtoToUser(userPostPutDto);
        user.setActive(true);
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
    }

    public UserDto updateUser(UserPostPutDto user, Long id) {
        User newUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user with" + id + " not found"));
        UserMapper.INSTANCE.updateUserFromUserUpdateDto(newUser, user);
        userRepository.save(newUser);
        return UserMapper.INSTANCE.userToUserDto(newUser);
    }

    public UserDto deactivateUser(Long id) {
        User newUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user with" + id + " not " +
                "found"));
        newUser.setActive(false);
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(newUser));
    }

    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserMapper.INSTANCE::userToUserDto).orElseThrow(() -> new NotFoundException("user with" + email + " not found"));
    }

    public UserEmailDto getEmailByUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user with id: " + userId + " not found"));
        return UserMapper.INSTANCE.userEmailDtoFromUser(user);
    }

    public List<UserDto> search(String firstName, String lastName, String email) {
        List<User> users = userRepository.findUsers(firstName, lastName, email)
                .orElseThrow(() -> new NotFoundException("No users found matching the criteria"));
        return users.stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toList());
    }

    public IsUserActiveDto isUserActive(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with" + email +
                " not found"));

        if (!user.isActive()) {
            emailServiceClient.sendUserActivationEmailToEmailService(email);
        }
        return UserMapper.INSTANCE.userToIsAUserActiveDTO(user);
    }

    public void setPassword(UserSetPasswordDto userSetPasswordDTO) {
        User user = userRepository.findByEmail(userSetPasswordDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPassword(passwordEncoder.encode(userSetPasswordDTO.getPassword()));
        user.setActive(true);
        userRepository.save(user);
    }
}
