package com.todo.dto.response;

public class AuthResponse {

	private String jwt;

	private String userRole;

	private Long userId;

	private boolean isSuccess;

	public AuthResponse() {
		super();
	}

	public AuthResponse(String jwt, String userRole, Long userId, boolean isSuccess) {
		super();
		this.jwt = jwt;
		this.userRole = userRole;
		this.userId = userId;
		this.isSuccess = isSuccess;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}
