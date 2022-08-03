package com.training.taskmanger.security.jwt;

import com.training.taskmanger.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${exalt.app.jwtSecret}")
  private String jwtSecret;

  @Value("${exalt.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${exalt.app.jwtCookieName}")
  private String jwtCookie;

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      LOGGER.info("getJwtFromCookies :: Got token from cookies : " + cookie.getValue());
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsernameAndId(userPrincipal.getUsername(),userPrincipal.getId());
    LOGGER.info("generateJwtCookie :: Token generated : " + jwt);
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
    LOGGER.info("generateJwtCookie :: Generate token cookie : " + cookie);
    return cookie;
  }

  public String generateTokenFromUsernameAndId(String username,int id) {
    LOGGER.debug("generateTokenFromUsername :: Generating token from user name : " + username);
    return Jwts.builder()
            .setSubject(username)
            .setId(String.valueOf(id))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }


  // Remove token from cookie
  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    LOGGER.info("getCleanJwtCookie :: Toke removed from cookie : " + cookie);
    return cookie;
  }

  public String getUserNameFromJwtToken(String token) {
    LOGGER.debug("getUserNameFromJwtToken :: Getting username from token..");
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public String getIdFromJwtToken(String token) {
    LOGGER.debug("getUserNameFromJwtToken :: Getting id from token..");
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getId();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      LOGGER.info("validateJwtToken :: Token validated");
      return true;
    } catch (SignatureException e) {
      LOGGER.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      LOGGER.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOGGER.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOGGER.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOGGER.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

}
