package rs.edu.raf.userservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import rs.edu.raf.userservice.domains.dto.user.CreateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UpdateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UserDto;
import rs.edu.raf.userservice.domains.exceptions.ForbiddenException;
import rs.edu.raf.userservice.domains.exceptions.NotFoundException;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.UserRepository;
import rs.edu.raf.userservice.services.UserService;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceApplicationTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void testLoadUserByUsername() {
        User user = createDummyUser("pera123@gmail.com");

        given(userRepository.findByEmail("pera123@gmail.com")).willReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("pera123@gmail.com");

        assertEquals(userDetails.getUsername(), user.getEmail());
        assertEquals(userDetails.getPassword(), user.getPassword());
    }

    @Test
    public void testLoadUserByUsername_FailUserIsNull() {

        given(userRepository.findByEmail("pera123@gmaill.com")).willReturn(null);

        assertThrows(Exception.class, () -> userService.loadUserByUsername("pera123@gmail.com"));
    }

    @Test
    public void testLoadUserByUsername_UserIsNotActive() {
        User user = createDummyUser("pera123@gmail.com");
        user.setIsActive(false);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        assertThrows(ForbiddenException.class, () -> userService.loadUserByUsername("pera123@gmail.com"));
    }

    @Test
    public void getUserByIdTest() {
        User user = createDummyUser("pera123@gmail.com");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(1L);
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getJmbg(), user.getJmbg());

    }

    @Test
    public void getUserByIdTest_NotFound() {

        given(userRepository.findById(1L)).willReturn(null);

        assertThrows(Exception.class, () -> userService.getUserById(1L));

    }


    @Test
    public void addUserTest() {
        CreateUserDto createUserDto = createDummyCreateUserDto("pera123@gmail.com");
        User user = createDummyUser("pera123@gmail.com");
        user.setAddress(null);
        user.setPassword(null);
        user.setUserId(null);
        given(userRepository.save(user)).willReturn(user);

        UserDto userDto = userService.addUser(createUserDto);

        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getJmbg(), user.getJmbg());
    }

    @Test
    public void addUserTest_FailEmail() {
        CreateUserDto createUserDto = createDummyCreateUserDto("pera123@daa");

        assertThrows(ValidationException.class, () -> userService.addUser(createUserDto));
    }

    @Test
    public void addUserTest_FailJMBG() {
        CreateUserDto createUserDto = createDummyCreateUserDto("pera123@gmail.com");
        createUserDto.setJmbg("1234");

        assertThrows(ValidationException.class, () -> userService.addUser(createUserDto));
    }

    @Test
    public void deactivateUserTest() {
        User user = createDummyUser("pera123@gmail.com");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);
        UserDto userDto = userService.deactivateUser(1L);

        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getJmbg(), userDto.getJmbg());
        assertFalse(userDto.getIsActive());
    }

    @Test
    public void deactivateUserTest_Fail() {
        User user = createDummyUser("pera123@gmail.com");

        given(userRepository.findById(1L)).willReturn(null);

        assertThrows(NullPointerException.class, () -> userService.deactivateUser(1L));
    }

    @Test
    public void updateUserTest() {
        User user = createDummyUser("pera123@gmail.com");
        UpdateUserDto updateUserDto = createDummyUpdateUserDto();
        updateUserDto.setFirstName("Mika");
        updateUserDto.setLastName("Mikic");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        UserDto userDto = userService.updateUser(updateUserDto, 1L);

        assertEquals(updateUserDto.getEmail(), userDto.getEmail());
        assertEquals(updateUserDto.getFirstName(), userDto.getFirstName());
        assertEquals(updateUserDto.getLastName(), userDto.getLastName());
    }

    @Test
    public void updateUserTest_Fail() {

        given(userRepository.findById(1L)).willReturn(null);

        assertThrows(NullPointerException.class, () -> userService.deactivateUser(1L));
    }

    @Test
    public void getUsersTest() {
        User user1 = createDummyUser("pera123@gmail.com");
        User user2 = createDummyUser("mika123@gmail.com");
        user2.setFirstName("Mika");
        user2.setLastName("Mikic");
        user2.setJmbg("0987654321098");

        List<User> users = List.of(user1, user2);

        given(userRepository.findAll()).willReturn(users);

        List<UserDto> userDtos = userService.getUsers();

        for (UserDto udto : userDtos) {
            boolean found = false;
            for (User u : users) {
                if (udto.getEmail().equals(u.getEmail()) &&
                        udto.getJmbg().equals(u.getJmbg())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("User not found");
            }
        }
    }

    @Test
    public void getUsers_Empty() {
        List<User> users = List.of();

        given(userRepository.findAll()).willReturn(users);

        assertThrows(NotFoundException.class, () -> userService.getUsers());
    }

    @Test
    public void getUserByEmailTest() {
        User user = createDummyUser("pera123@gmail.com");

        given(userRepository.findByEmail("pera123@gmail.com")).willReturn(Optional.of(user));

        UserDto userDto = userService.getUserByEmail("pera123@gmail.com");

        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getJmbg(), userDto.getJmbg());
    }

    @Test
    public void getUserByEmailTest_Fail() {

        given(userRepository.findByEmail("pera123@gmail.com")).willReturn(null);

        assertThrows(NullPointerException.class, () -> userService.getUserByEmail("pera123@gmail.com"));
    }

    @Test
    public void getUserByPhoneNumberTest() {
        User user = createDummyUser("pera123@gmail.com");

        given(userRepository.findByPhoneNumber("+3123214254")).willReturn(Optional.of(user));

        UserDto userDto = userService.getUserByMobileNumber("+3123214254");

        assertEquals(user.getPhoneNumber(), userDto.getPhoneNumber());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getJmbg(), userDto.getJmbg());
    }

    @Test
    public void getUserByPhoneNumberTest_Fail() {

        given(userRepository.findByPhoneNumber("+3123214254")).willReturn(null);

        assertThrows(NullPointerException.class, () -> userService.getUserByMobileNumber("+3123214254"));
    }

    @Test
    public void getUserByJMBGTest() {
        User user = createDummyUser("pera123@gmail.com");

        given(userRepository.findByJmbg("1234567890123")).willReturn(Optional.of(user));

        UserDto userDto = userService.getUserByJmbg("1234567890123");

        assertEquals(user.getPhoneNumber(), userDto.getPhoneNumber());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getJmbg(), userDto.getJmbg());
    }

    @Test
    public void getUserByJMBG_Fail() {

        given(userRepository.findByPhoneNumber("+3123214254")).willReturn(null);

        assertThrows(NullPointerException.class, () -> userService.getUserByMobileNumber("+3123214254"));
    }

    @Test
    public void searchTestSuccess() {
        User user = createDummyUser("pera123@gmail.com");
        List<User> users = new ArrayList<>();
        users.add(user);

        given(userRepository.findUsers("p", null, null)).willReturn(Optional.of(users));

        List<UserDto> actual = userService.search("p", null, null);
        List<UserDto> expected = new ArrayList<>();
        expected.add(createDummyUserDto());

        assertEquals(expected, actual);
    }

    private CreateUserDto createDummyCreateUserDto(String email) {
        CreateUserDto user = new CreateUserDto();
        user.setFirstName("Pera");
        user.setLastName("Peric");
        user.setJmbg("1234567890123");
        user.setDateOfBirth("123");
        user.setGender("M");
        user.setPhoneNumber("+3123214254");
        user.setEmail(email);

        return user;
    }

    private User createDummyUser(String email) {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Pera");
        user.setLastName("Peric");
        user.setJmbg("1234567890123");
        user.setDateOfBirth(123L);
        user.setGender("M");
        user.setPhoneNumber("+3123214254");
        user.setAddress("Mika Mikic 13");
        user.setEmail(email);
        user.setPassword("pera1234");
        user.setIsActive(true);

        return user;
    }

    private UpdateUserDto createDummyUpdateUserDto() {
        UpdateUserDto user = new UpdateUserDto();
        user.setFirstName("Pera");
        user.setLastName("Peric");
        user.setDateOfBirth(123L);
        user.setGender("M");
        user.setPhoneNumber("+3123214254");
        user.setAddress("Mika Mikic 13");
        user.setEmail("pera123@gmail.com");
        user.setIsActive(true);

        return user;
    }

    private UserDto createDummyUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUserId(1L);
        userDto.setFirstName("Pera");
        userDto.setLastName("Peric");
        userDto.setJmbg("1234567890123");
        userDto.setDateOfBirth(123L);
        userDto.setGender("M");
        userDto.setPhoneNumber("+3123214254");
        userDto.setAddress("Mika Mikic 13");
        userDto.setEmail("pera123@gmail.com");
        userDto.setIsActive(true);

        return userDto;
    }
}
