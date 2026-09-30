package com.gather_link.config;
import com.gather_link.repository.UserRepository;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean UserDetailsService users(UserRepository repository){return username->{var user=repository.findByUsername(username);if(user==null)throw new UsernameNotFoundException("Invalid credentials");return User.withUsername(user.getUsername()).password(user.getPassword()).roles("USER").build();};}
 @Bean SecurityFilterChain security(HttpSecurity http,UserRepository repository)throws Exception{
  return http.authorizeHttpRequests(a->a.dispatcherTypeMatchers(jakarta.servlet.DispatcherType.FORWARD,jakarta.servlet.DispatcherType.ERROR).permitAll().requestMatchers("/","/login","/register","/resources/**","/error").permitAll().anyRequest().authenticated())
   .formLogin(f->f.loginPage("/login").successHandler((request,response,authentication)->{request.getSession().setAttribute("loggedInUser",repository.findByUsername(authentication.getName()));response.sendRedirect(request.getContextPath()+"/home");}).failureUrl("/login?error"))
   .logout(l->l.logoutSuccessUrl("/login?logout"))
   .build();
 }
}
