package com.training.taskmanger.controllers;


import com.training.taskmanger.entity.User;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.security.http.request.LoginRequest;
import com.training.taskmanger.security.http.request.SignupRequest;
import com.training.taskmanger.security.http.response.MessageResponse;
import com.training.taskmanger.security.jwt.AuthTokenFilter;
import com.training.taskmanger.security.jwt.JwtUtils;
import com.training.taskmanger.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  AuthTokenFilter authTokenFilter;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    LOGGER.info("Authentication successfully");

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
    setLogoutStatus(false);
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(new MessageResponse("Login Successfully!!"));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(signUpRequest.getName(),
                         encoder.encode(signUpRequest.getPassword()),
                         signUpRequest.getEmail(),
                         signUpRequest.getUsername()
    );

    userRepository.save(user);
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }

  @PostMapping("/signoutAll")
  public ResponseEntity<?> logoutAll(){
    setLogoutStatus(true);
    return ResponseEntity.ok().body(new MessageResponse("Signout frm all places"));
  }

  private void setLogoutStatus(boolean status) {
    int userId = authTokenFilter.getUserId();
    Optional<User> user = userRepository.findById(userId);
    if(user == null){
      throw new NotFoundException("User not found");
    }
    user.get().setSignout(status);
    userRepository.save(user.get());
  }
}
