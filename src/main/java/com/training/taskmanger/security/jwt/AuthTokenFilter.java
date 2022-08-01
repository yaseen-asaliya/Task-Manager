package com.training.taskmanger.security.jwt;

import com.training.taskmanger.entity.User;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.service.UserServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthTokenFilter extends OncePerRequestFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private UserServiceImplementation userServiceImplementation;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AuthTokenFilter authTokenFilter;
  private int userId;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {

      String jwt = parseJwt(request);

      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        userId = Integer.parseInt(jwtUtils.getIdFromJwtToken(jwt));
        LOGGER.info("doFilterInternal :: got username = " + username + " with id = " + userId + " from jwt.");

        UserDetails userDetails = userServiceImplementation.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails,null,null);
        
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

    } catch (Exception e) {
      LOGGER.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String jwt = jwtUtils.getJwtFromCookies(request);
    LOGGER.info("parseJwt :: Token after parse : " + jwt);
    return jwt;
  }

  public int getUserId() {
    return userId;
  }
  private boolean isSignout() {
    int userId = authTokenFilter.getUserId();
    Optional<User> user = userRepository.findById(userId);
    if (user == null) {
      throw new NotFoundException("User not found");
    }
    if (user.get().getSignout()) {
      return true;
    }
    return false;
  }

}
