package com.gather_link.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gather_link.exceptions.UserNotFoundException;
import com.gather_link.model.Users;
import com.gather_link.repository.UserRepository;

@Service
public class UserService {
 @Autowired private org.springframework.security.crypto.password.PasswordEncoder encoder;
	
	@Autowired
    private UserRepository userRepository;

    public void create(Users user) {
        if(user.getUsername()==null || !user.getUsername().matches("[A-Za-z0-9_-]{3,50}")) throw new IllegalArgumentException("Username must be 3–50 letters, numbers, underscores or dashes");
        if(user.getEmail()==null || !user.getEmail().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+") || user.getEmail().length()>100) throw new IllegalArgumentException("Enter a valid email");
        validatePassword(user.getPassword());
        if(userRepository.findByUsername(user.getUsername())!=null || userRepository.findByEmail(user.getEmail())!=null) throw new IllegalArgumentException("Username or email already registered");
        user.setUserId(null);user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

 public void validatePassword(String password){if(password==null || password.length()<12 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)throw new IllegalArgumentException("Password must contain 12–72 characters");}
 public String hashPassword(String password){validatePassword(password);return encoder.encode(password);}
	public void deleteUser(Long user_id) throws UserNotFoundException {
		userRepository.deleteById(user_id);
	}

	public void updateUser(Users user) throws UserNotFoundException {
		userRepository.save(user);
	}
	
	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}
	
	public Users getByUserId(Long user_id) throws UserNotFoundException {
		Optional<Users> user = userRepository.findById(user_id);
		if (user.isEmpty()) {
			throw new UserNotFoundException("User not found by ID");
		}
		return user.get();
	}
	
	public Users getUserByUsername(String username) {
	    return userRepository.findByUsername(username);
	}

	public Users getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
}