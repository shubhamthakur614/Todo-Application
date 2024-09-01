package com.todo.controller.auth;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.config.JwtTokenProvider;
import com.todo.dto.request.AuthRequest;
import com.todo.dto.request.SignupRequest;
import com.todo.dto.response.AuthResponse;
import com.todo.dto.response.UserDto;
import com.todo.repository.UserRepository;
import com.todo.service.auth.AuthService;
import com.todo.service.jwt.CustomUserDetails;
import com.todo.service.jwt.CustomUserDetailsService;

@RestController
@RequestMapping("/auth")
//@CrossOrigin("*")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
		if (authService.hasUserWithEmail(signupRequest.getEmail())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
					.body("User Allready Exist with this email: " + signupRequest.getEmail());
		}
		UserDto createdDto = authService.signupUser(signupRequest);
		if (createdDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Created!");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);

	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody AuthRequest authRequest) {
		String username = authRequest.getEmail();
		String password = authRequest.getPassword();

//		System.out.println(username + " ----- " + password);

		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateToken(authentication);
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	    Long userId = userDetails.getId();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String userRole = "";
		for (GrantedAuthority authority : authorities) {
			userRole = authority.getAuthority();
		}
		AuthResponse authResponse = new AuthResponse(token, userRole,userId, true);

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

	}

	private Authentication authenticate(String username, String password) {
		CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

		System.out.println("sign in userDetails - " + userDetails);

		if (userDetails == null) {
			System.out.println("sign in userDetails - null " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			System.out.println("sign in userDetails - password not match " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
