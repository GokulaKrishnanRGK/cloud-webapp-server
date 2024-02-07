package com.neu.csye6225.cloud.config;

import com.neu.csye6225.cloud.model.User;
import com.neu.csye6225.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private UserService userService;

  /*@Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    String[] securePaths = new String[]{"/v1/user/{userId}"};
    String[] publicPaths = new String[]{"/healthz","/v1/user"};
    http.csrf(csrf -> csrf.configure(http)).httpBasic(withDefaults()).authenticationManager(authenticationManager());
    http.authorizeHttpRequests(auth -> auth.requestMatchers(publicPaths).permitAll().requestMatchers(securePaths).authenticated());
    return http.build();
  }*/

  @Bean("passwordEncoder")
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /*@Bean
  public AuthenticationManager authenticationManager() {
    AuthenticationManagerImpl dap = new AuthenticationManagerImpl();
    dap.setPasswordEncoder(passwordEncoder());
    dap.setUserService(this.userService);
    return dap;
  }*/

  public static User getAuthUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }

}
