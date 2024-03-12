package rs.edu.raf.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.UserRepository;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService, UserServiceInterface {

    @Autowired
    private UserRepository userRepository;
    private final Pattern emailPattern = Pattern.compile("^[a-z0-9_.-]+@(.+)$");
    private final Pattern jmbgPattern = Pattern.compile("[0-9]{13}");

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    //Po defaultu je ovo metoda koja se poziva kada se korisnik loguje
    //Ovde se proverava da li korisnik postoji u bazi
    //Ako postoji, vraca se objekat koji implementira UserDetails interfejs
    //Ovaj objekat se koristi za autentifikaciju korisnika
    //Ukoliko korisnik ne postoji, baca se izuzetak
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User with the email: " + email + " not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
    @Override
    public Optional<UserDto> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserMapper.INSTANCE::userToUserDto);//TODO dodaj exception ako treba
    }

    @Override
    public UserDto addUser(CreateUserDto createUserDto) {
        if(!emailPattern.matcher(createUserDto.getEmail()).matches()) {
            throw new ValidationException("invalid email");
        }
        if(!jmbgPattern.matcher(createUserDto.getJmbg()).matches()) {
            throw new ValidationException("invalid jmbg");
        }
        User user = UserMapper.INSTANCE.userCreateDtoToUser(createUserDto);
        user.setIsActive(true);
        userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    @Override
    public UserDto deactivateUser(Long id) {
        User newUser = userRepository.findById(id).orElseThrow();//TODO dodaj exception
        newUser.setIsActive(false);
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(newUser));
    }

    @Override
    public UserDto updateUser(UpdateUserDto user, Long id) {
        User newUser = userRepository.findById(id).orElseThrow();//TODO dodaj exception
        UserMapper.INSTANCE.updateUserFromUserUpdateDto(newUser, user);
        userRepository.save(newUser);
        return UserMapper.INSTANCE.userToUserDto(newUser);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> listaUsera = userRepository.findAll();
        return listaUsera.stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserMapper.INSTANCE::userToUserDto).orElseThrow(() -> new NotFoundException("user not found"));
    }

    @Override
    public UserDto getUserByMobileNumber(String mobileNumber) {
        Optional<User> user = userRepository.findByPhoneNumber(mobileNumber);
        return user.map(UserMapper.INSTANCE::userToUserDto).orElseThrow(() -> new NotFoundException("user not found"));
    }

    @Override
    public UserDto getUserByJmbg(String jmbg) {
        Optional<User> user = userRepository.findByJmbg(jmbg);
        return user.map(UserMapper.INSTANCE::userToUserDto).orElseThrow(() -> new NotFoundException("user not found"));
    }
}
