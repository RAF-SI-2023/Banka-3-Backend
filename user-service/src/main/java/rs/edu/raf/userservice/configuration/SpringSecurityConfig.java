package rs.edu.raf.userservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import rs.edu.raf.userservice.filters.JwtFilter;
import rs.edu.raf.userservice.services.UserService;

@EnableWebSecurity
@EnableAsync
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
    private final UserService userService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SpringSecurityConfig(UserService userService, JwtFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                //TODO
                //Role:ADMIN_USER,
                //     READ_USERS,
                //     CREATE_USERS,
                //     UPDATE_USERS,
                //     DELETE_USERS
//                .antMatchers("/api/users/login").permitAll()
//                .antMatchers("/api/users/add/**").hasAuthority("can_create_users")
//                .antMatchers("/api/users/update/**").hasAuthority("can_update_users")




                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //Prestace da se crveni kada se UserService sredi. Cekamo kolege da zavrse to
        httpSecurity.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
