package sn.l3gl.banque.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sn.l3gl.banque.api.model.AppUser;
import sn.l3gl.banque.api.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin@gmail.com").isEmpty()) {
            AppUser admin = new AppUser("admin@gmail.com", passwordEncoder.encode("passer123"));
            userRepository.save(admin);
            System.out.println("Default user created: admin@gmail.com / passer123");
        }
    }
}
