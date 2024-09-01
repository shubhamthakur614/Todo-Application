package com.todo.service.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.todo.dto.request.SignupRequest;
import com.todo.dto.response.UserDto;
import com.todo.dto.util.UserUtil;
import com.todo.entity.User;
import com.todo.enums.UserRole;
import com.todo.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void creatDefaultAccount() {
		Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
		if (optionalUser.isEmpty()) {
			User user = new User();
			user.setName("admin");
			user.setEmail("admin@test.com");
			user.setPassword(new BCryptPasswordEncoder().encode("admin@123"));
			user.setUserRole(UserRole.ADMIN);
			userRepository.save(user);
			System.out.println("Admin Account created successfully!");
		} else {
			System.out.println("Admin account is Already exist!");
		}
	}

	@Override
	public UserDto signupUser(SignupRequest signupRequest) {
		User user = UserUtil.getUserEntity(signupRequest);
		User savedUser = userRepository.save(user);
		UserDto userDto = UserUtil.getUserDto(savedUser);
		return userDto;
	}

	@Override
	public boolean hasUserWithEmail(String email) {
		return userRepository.findFirstByEmail(email).isPresent();
	}
}
