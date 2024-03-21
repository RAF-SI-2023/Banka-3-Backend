package rs.edu.raf.userservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.edu.raf.userservice.domains.dto.user.*;
import rs.edu.raf.userservice.domains.exceptions.ForbiddenException;
import rs.edu.raf.userservice.domains.exceptions.NotFoundException;
import rs.edu.raf.userservice.domains.mappers.UserMapper;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.UserRepository;
import rs.edu.raf.userservice.utils.EmailServiceClient;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService, UserServiceInterface {

    private final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private final Pattern jmbgPattern = Pattern.compile("[0-9]{13}");
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailServiceClient emailServiceClient;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not " +
                "found"));

        if (user == null) {
            throw new UsernameNotFoundException("User with the email: " + email + " not found");
        }
        if (!user.getIsActive()) {
            throw new ForbiddenException("user not active");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserMapper.INSTANCE::userToUserDto).orElseThrow(() -> new NotFoundException("user with" + id + " not found"));
    }

    @Override
    public UserDto addUser(CreateUserDto createUserDto) {
        if (!emailPattern.matcher(createUserDto.getEmail()).matches()) {
            throw new ValidationException("invalid email");
        }
        if (!jmbgPattern.matcher(createUserDto.getJmbg()).matches()) {
            throw new ValidationException("invalid jmbg");
        }
        User user = UserMapper.INSTANCE.userCreateDtoToUser(createUserDto);
        user.setIsActive(true);
        userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    @Override
    public UserDto deactivateUser(Long id) {
        User newUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user with" + id + " not " +
                "found"));
        newUser.setIsActive(false);
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(newUser));
    }

    @Override
    public UserDto updateUser(UpdateUserDto user, Long id) {
        User newUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user with" + id + " not " +
                "found"));//TODO dodaj exception
        UserMapper.INSTANCE.updateUserFromUserUpdateDto(newUser, user);
        userRepository.save(newUser);
        return UserMapper.INSTANCE.userToUserDto(newUser);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> listaUsera = userRepository.findAll();
        if (listaUsera.isEmpty()) {
            throw new NotFoundException("not found users");
        }
        return listaUsera.stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserMapper.INSTANCE::userToUserDto).orElseThrow(() -> new NotFoundException("user with" + email + " not found"));
    }

    @Override
    public List<UserDto> search(String firstName, String lastName, String email) {
        List<User> users = userRepository.findUsers(firstName, lastName, email)
                .orElseThrow(() -> new NotFoundException("No users found matching the criteria"));
        return users.stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toList());
    }

    public IsUserActiveDTO isUserActive(String email){
        User user = userRepository.findByEmail(email)
                                    .orElseThrow(()-> new NotFoundException("User with" + email + " not found"));
        return UserMapper.INSTANCE.userToIsAUserActiveDTO(user);
    }

    public String setPassword(SetPasswordDTO setPasswordDTO){
        User user = userRepository.findByEmail(setPasswordDTO.getEmail())
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPassword(passwordEncoder.encode(setPasswordDTO.getPassword()));
        userRepository.save(user);
        return "Successfully updated password for " + setPasswordDTO.getEmail();
    }

    public String resetPassword(ResetUserPasswordDTO resetPasswordDTO) {
        System.out.println(resetPasswordDTO.getEmail());
        User user = userRepository.findByEmail(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        userRepository.save(user);
        return "Successfully reseted password for " + resetPasswordDTO.getEmail();
    }
}
