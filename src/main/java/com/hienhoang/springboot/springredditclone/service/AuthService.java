package com.hienhoang.springboot.springredditclone.service;

import com.hienhoang.springboot.springredditclone.dto.AuthenticationResponse;
import com.hienhoang.springboot.springredditclone.dto.LoginRequest;
import com.hienhoang.springboot.springredditclone.dto.RegisterRequest;
import com.hienhoang.springboot.springredditclone.exception.SpringRedditException;
import com.hienhoang.springboot.springredditclone.model.NotificationEmail;
import com.hienhoang.springboot.springredditclone.model.User;
import com.hienhoang.springboot.springredditclone.model.VerificationToken;
import com.hienhoang.springboot.springredditclone.repository.UserRepository;
import com.hienhoang.springboot.springredditclone.repository.VerificationTokenRepository;
import com.hienhoang.springboot.springredditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import static com.hienhoang.springboot.springredditclone.util.Constants.ACTIVATION_EMAIL;

@Service
@AllArgsConstructor
public class AuthService {
    // Nothing wrong with @Autowired, but Spring recommend to use constructor injection
    // Link : https://reflectoring.io/constructor-injection/#:~:text=Constructor%20injection%20helps%20us%20to,address%20proper%20separation%20of%20concerns.
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    // Contains main business logic to register user
    // We interact with DB so it is required to use @Transactional
    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false); // user is not activated yet

        // Before saving the user, we need to encrypt password
        // Save the password in plain text is bad idea, especially when db is compromised by hacker
       userRepository.save(user);

       String token = generateVerificationToken(user);
       mailService.sendMail(new NotificationEmail("Please activate your account",
               user.getEmail(), "Thank you for signing up to Spring Reddit, " +
               "please click on the url on below to activate your account: " +
               ACTIVATION_EMAIL + "/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        // Persists this token to the DB so we can verify the account in some days
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;

    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        // Query corresponding user associating with this token and enable this user
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken) {
        String userName = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new SpringRedditException("User not found with name " + userName));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        // Create authentication token and use Authentication Manager to perform login
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        // Check if user is logged in or not
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());

    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + principal.getUsername() + " not found"));
    }
}
