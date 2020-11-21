package com.example.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


import com.example.Model.User;
import com.example.Repository.UserRepository;
import com.example.Service.UserServiceImpl;



@Controller
public class UserController {
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private UserRepository userRepository;
	
	
	  @GetMapping("/")
	    public String root() {
	        return "index";
	  
	    }
	  @GetMapping("/login")
	    public String login() {
		 return "login";
	    }
	  
	  @GetMapping("/logout")
	  public String logout() {
	 	return "index"; 
	 	 
	  }
	  
	  @GetMapping("/user/home")
	    public ModelAndView  getHome() {
		  ModelAndView model = new ModelAndView();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findUserByEmail(auth.getName());
			
			model.addObject("user", user);
			model.addObject("users",userRepository.findAll());
			model.setViewName("user/index");
			
			return model;
			
	    }


	    @GetMapping("/registration")
	    public ModelAndView  addNewUser() {
	    	ModelAndView modelAndView = new ModelAndView();
			User user = new User();
			modelAndView.addObject("user", user);
			modelAndView.setViewName("register");
			return modelAndView;
	    }
		@PostMapping("/registration")
		public ModelAndView RegisterNewUser(@Validated User user, BindingResult bindingResult) {
			ModelAndView modelAndView = new ModelAndView();
			User userExists = userService.findUserByEmail(user.getEmail());
			if (userExists != null) {
				bindingResult
						.rejectValue("email", "error.user",
								"There is already a user registered with the email provided");
			}
			if (bindingResult.hasErrors()) {
				modelAndView.setViewName("register");
			} else {
				userService.saveUser(user);
				modelAndView.addObject("successMessage", "User has been registered successfully");
				modelAndView.addObject("user", new User());
				modelAndView.setViewName("register");
				
			}
			return modelAndView;
		}
		
		
		 @GetMapping("/access_denied")
		 public ModelAndView accessDenied() {
		  ModelAndView model = new ModelAndView();
		  model.setViewName("error/access_denied");
		  return model;
		 }
		 
		 ///
		///Update
		 @GetMapping("/user/edit/{id}")
			public String editUserProfile(@PathVariable("id") long id, Model model) {   
				Optional<User> user = userRepository.findById(id);  
				model.addAttribute("user", user);
				return "user/edit";
			}
		 
		 
		 @PostMapping("/user/update")
		 public String userProfileUpdate( @Validated User user, 
		   BindingResult result, Model model) {
		     if (result.hasErrors()) {
		        
		         return "/user/edit";
		     }
		     userRepository.updateUser(user.getId(), user.getUsername(), user.getEmail());
		     return "redirect:/user/home";
		 }
		 ///Delete
		 @GetMapping("/user/delete/{id}")
		 public String deleteUser(@PathVariable("id") long id, Model model) {
			 userService.deleteFromUserRole(id);
			 userRepository.deleteById(id);
		     return "redirect:/user/home";
		 }
		 //Detail
		 @GetMapping("/user/detail/{id}")
			public String userProfileDetail(@PathVariable("id") long id, Model model) {	   
				Optional<User> user = userRepository.findById(id);  
				model.addAttribute("user", user.get());
				return "/user/detail";
			}
		 
	
		 
}
