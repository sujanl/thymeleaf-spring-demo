package com.sujan.thymleafdemo.controller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sujan.thymleafdemo.daos.LoginRequest;
import com.sujan.thymleafdemo.daos.UserEditRequest;
import com.sujan.thymleafdemo.model.User;
import com.sujan.thymleafdemo.repository.UserRepository;
import com.sujan.thymleafdemo.service.MailService;
import com.sujan.thymleafdemo.utils.ImageUtils;
import com.sujan.thymleafdemo.utils.PasswordUtils;

@Controller
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	MailService mailService;
	
	@GetMapping("/signup")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/adduser")
	public String addUser(User user, Model model) {
		User chkUser = userRepository.findByEmail(user.getEmail());
		if(chkUser!=null) {
			model.addAttribute("message", "Email already in use.");
			return "signup";
		}
		String pass = new PasswordUtils().generatePassword(10);
		user.setPassword(pass);
		try {
			mailService.sendSimpleEmail(user.getName(), user.getEmail(), pass);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			return "login";
		}else {
			LOG.info("Login Success");
			return "redirect:/home/"+user.getId();
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
	public String updateProfile(@PathVariable long id, UserEditRequest editRequest) {
		LOG.info("/save invoked.");
		User user = userRepository.getOne(id);
		user.setName(editRequest.getName());
		user.setAddress(editRequest.getAddress());
		userRepository.save(user);
		
		return "redirect:/home/"+user.getId();
	}
	
	@GetMapping("/change-profile-pic/{id}")
	public String changeProfilePic(@PathVariable long id) {
		return "change-profile-pic";
	}
	
	@PostMapping("/confirm/pic/{id}")
	public String confirmPic(@PathVariable(value = "id") long id, 
			RedirectAttributes redirectAttributes, 
			@RequestParam(value="image") String image) {
		User user = userRepository.getOne(id);
		if(image.length()!=0) {
			user.setProfilePicture(ImageUtils.saveImage(image));
			userRepository.save(user);	
		}
		redirectAttributes.addFlashAttribute("id", user.getId());
		return "redirect:/success";
	}
	

	@GetMapping("/success")
	public String home(@ModelAttribute("id") long id, Model model) {
		LOG.info("Id "+id);
		model.addAttribute("id", id);
		return "success";
	}
	
	@GetMapping("/home/{id}")
	public String goHome(@PathVariable long id, Model model) {
		model.addAttribute("user", userRepository.getOne(id));
		return "home";
	}
	
}
