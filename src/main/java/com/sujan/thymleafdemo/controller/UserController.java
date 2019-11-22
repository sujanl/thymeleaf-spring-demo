package com.sujan.thymleafdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sujan.thymleafdemo.daos.LoginRequest;
import com.sujan.thymleafdemo.daos.UserEditRequest;
import com.sujan.thymleafdemo.model.User;
import com.sujan.thymleafdemo.repository.UserRepository;
import com.sujan.thymleafdemo.utils.ImageUtils;

@Controller
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/signup")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/adduser")
	public String addUser(User user) {
		user.setPassword("admin");
		userRepository.save(user);
		LOG.info("user created: ID = "+ user.getId());
		return "index";
	}
	
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}
	
	@PostMapping("/login")
	public String login(LoginRequest request, Model model) {
		User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
		if(user == null) {
			LOG.info("Login Failed!");
			model.addAttribute("message", "Login Failed!");
			return "index";
		}else {
			LOG.info("Login Success");
			model.addAttribute("user", user);
			return "home";
		}
		
	}
	
	@GetMapping("/edit-profile/{id}")
	public String showEditForm(@PathVariable long id, Model model) {
		User user = userRepository.getOne(id);
		model.addAttribute("user", user);
		model.addAttribute("editRequest", new UserEditRequest());
		return "edit-profile";
	}
	
	@PostMapping("/save/{id}")
	public String updateProfile(@PathVariable long id, UserEditRequest editRequest, Model model) {
		LOG.info("/save invoked.");
		User user = userRepository.getOne(id);
		user.setName(editRequest.getName());
		user.setAddress(editRequest.getAddress());
		userRepository.save(user);
		
		model.addAttribute("user", user);
		return "home";
	}
	
	@GetMapping("/change-profile-pic/{id}")
	public String changeProfilePic(@PathVariable long id) {
		return "change-profile-pic";
	}
	
	@PostMapping("/confirm/pic/{id}")
	public String confirmPic(@PathVariable(value = "id") long id, Model model, @RequestParam(value="image") String image) {
		User user = userRepository.getOne(id);
		user.setProfilePicture(ImageUtils.saveImage(image));
		LOG.info("image dir: "+user.getProfilePicture());
		userRepository.save(user);
		model.addAttribute("user", user);
		return "home";
	}
	
}
