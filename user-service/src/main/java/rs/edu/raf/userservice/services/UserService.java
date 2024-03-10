package rs.edu.raf.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.UserRepository;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


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
}
