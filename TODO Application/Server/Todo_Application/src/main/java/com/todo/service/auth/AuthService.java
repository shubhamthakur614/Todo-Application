package com.todo.service.auth;

import com.todo.dto.request.SignupRequest;
import com.todo.dto.response.UserDto;

public interface AuthService {

	UserDto signupUser(SignupRequest signupRequest);

	boolean hasUserWithEmail(String email);
}
