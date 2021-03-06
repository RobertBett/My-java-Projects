package com.example.admindashboard.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.admindashboard.models.Role;
import com.example.admindashboard.models.User;
import com.example.admindashboard.services.RoleService;
import com.example.admindashboard.services.UserService;
import com.example.admindashboard.validations.UserValidator;

@Controller
public class UserController {
	private UserService userService;
	@Autowired
	private RoleService roleService;
	private UserValidator userValidator;
	
	public UserController(UserService userService,RoleService roleService,UserValidator userValidator){
		this.userService = userService;
		this.roleService = roleService;
		this.userValidator = userValidator;
	}
	
	@RequestMapping(value={"/login","/register"})
	public String login(Model model,@RequestParam(value="error",required=false) String error,@RequestParam(value="logout",required=false) String logout){
		if(error != null){model.addAttribute("errorMessage","Invalid Credentials.");}
		if(logout != null){model.addAttribute("logoutMessage","Logout Successful");}
		
		model.addAttribute("user",new User());
		return "login_register";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User user,BindingResult res,Model model, @ModelAttribute("role") Role role, HttpSession session){
		userValidator.validate(user,res);
		System.out.println(roleService.findByName("ROLE_ADMIN").getUsers().size());
		if(res.hasErrors()){return "login_register";}
		
		else if (roleService.findByName("ROLE_ADMIN").getUsers().size() < 2) {
			session.setAttribute( "id",user.getId() );
			userService.create(new String[]{"ROLE_USER","ROLE_ADMIN"}, user);
		}else{
			userService.create(new String[]{"ROLE_USER"}, user);
		}
		return "redirect:/login";
	}

	@RequestMapping("/superadmin")
	public String superadmin(Principal principal,Model model){
		model.addAttribute("user",userService.findByEmail(principal.getName()));
		model.addAttribute("users",userService.all());
		return "superadmin";
	}
	
	@RequestMapping("/admin")
	public String admin(Principal principal,Model model){		
		model.addAttribute("user",userService.findByEmail(principal.getName()));
		model.addAttribute("users",userService.all());
		return "admin";
	}
	
	@RequestMapping("/admin/delete/{id}")
	public String delete(@PathVariable("id") long id){
		userService.destroy(id);
		return "redirect:/admin";
	}
	
	@RequestMapping("/admin/promote/{id}")
	public String promote(@PathVariable("id") long id){
		User user = userService.getById(id);
		List<Role> userRoles = user.getRoles();
		userRoles.add(roleService.findByName("ROLE_ADMIN"));
		userService.update(user);
		
		return "redirect:/";
	}
	@RequestMapping("/superadmin/delete/{id}")
	public String superdelete(@PathVariable("id") long id){
		userService.destroy(id);
		return "redirect:/superadmin";
	}
	
	@RequestMapping("/superadmin/promote/{id}")
	public String superpromote(@PathVariable("id") long id){
		User user = userService.getById(id);
		List<Role> userRoles = user.getRoles();
		userRoles.add(roleService.findByName("ROLE_SUPERADMIN"));
		userService.update(user);
		
		return "redirect:/superadmin";
	}
	
	@RequestMapping("/superadmin/demote/{id}")
	public String demote(@PathVariable("id") long id){
		User user = userService.getById(id);
		List<Role> userRoles = user.getRoles();
		userRoles.add(roleService.findByName("ROLE_USER"));
		userService.update(user);
		
		return "redirect:/superadmin";
	}
	
	@RequestMapping(value={"/","/dashboard"})
	public String dashboard(Principal principal,Model model,HttpSession session){
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user",user);
		user.setUpdatedAt(new Date());
		session.setAttribute( "id",user.getId() );
		userService.update(user);
		
		if(user.isSuperAdmin()){
			return "redirect:/superadmin";
		}else if(user.isAdmin()){
			return "redirect:/admin";
		}else{
			return "dashboard";
		}
	}
}
