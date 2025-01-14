package it.epicode.u5_w3_d2.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String identifier)  {
        //Cerca l'utente per username o email
        AppUser user = appUserRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        //ora bisogna convertire l'entità appUser in UserDetails
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))))
                .build();
    }
}
