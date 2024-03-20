package rs.edu.raf.userservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.employee.SetPasswordDTO;
import rs.edu.raf.userservice.domains.dto.user.CreateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UpdateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UserDto;
import rs.edu.raf.userservice.domains.exceptions.ForbiddenException;
import rs.edu.raf.userservice.domains.exceptions.NotFoundException;
import rs.edu.raf.userservice.domains.mappers.UserMapper;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.UserRepository;

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
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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


    /**
     * Metoda koja preko repository update-uje user element i dodaje mu sifru koristeci UPDATE query.
     * Takodje "aktivira" nalog - postavlja isActive na true.
     * Poziva se iz userControllera na putanji /setPassword
     *
     */
    @Override
    public UserDto setUserPassword(SetPasswordDTO user1) {
        Optional<User> optionalUser = userRepository.findByEmail(user1.getEmail());
        System.out.println(optionalUser + " " + user1);

        User user = optionalUser.get();
        user.setIsActive(true);
        String password = passwordEncoder.encode(user1.getPassword());
        user.setPassword(password);
        userRepository.save(user);

        return optionalUser.map(UserMapper.INSTANCE::userToUserDto).orElseThrow(() -> new NotFoundException("user with" + user.getEmail() + " not found"));

    }


}
