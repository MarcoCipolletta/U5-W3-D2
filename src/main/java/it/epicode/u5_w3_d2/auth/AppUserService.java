package it.epicode.u5_w3_d2.auth;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AppUser registerUser(String Username ,String email, String password, List<Role> roles) {
        if (appUserRepository.existsByEmail(email)) {
            throw new EntityExistsException("Username già in uso");
        }
        if (appUserRepository.existsByUsername(Username)) {
            throw new EntityExistsException("Email già in uso");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setUsername(Username);
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.setRoles(roles);

        return appUserRepository.save(appUser);
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public String authenticateUser(String username, String password)  {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new SecurityException("Credenziali non valide", e);
        }
    }
}
