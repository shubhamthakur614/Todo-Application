package com.todo.dto.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.todo.dto.request.SignupRequest;
import com.todo.dto.response.UserDto;
import com.todo.entity.User;
import com.todo.enums.UserRole;

public class UserUtil {

	public static User getUserEntity(SignupRequest signupRequest) {
		User user = new User();
		user.setName(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		user.setUserRole(UserRole.EMPLOYEE);
		return user;

	}

	public static UserDto getUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		userDto.setUserRole(user.getUserRole());
		return userDto;

	}

}
