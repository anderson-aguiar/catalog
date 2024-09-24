package com.anderson.catalog.dto;

public class UserInsertDTO extends UserDTO {
	
	private String password;
	
	public UserInsertDTO() {
		super();
	}

	public UserInsertDTO(String password) {
		super();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
