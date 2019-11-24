package com.sujan.thymleafdemo.daos;

import javax.validation.constraints.NotBlank;

public class UserEditRequest {
	@NotBlank(message = "Name is mandatory!")
	private String name;
	
	@NotBlank(message = "Name is mandatory!")
	private String address;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
