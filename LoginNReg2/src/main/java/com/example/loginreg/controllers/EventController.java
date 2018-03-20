package com.example.loginreg.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.loginreg.models.Event;
import com.example.loginreg.models.Message;
import com.example.loginreg.models.User;
import com.example.loginreg.services.EventService;
import com.example.loginreg.services.MessageService;
import com.example.loginreg.services.UserService;



@Controller
@RequestMapping("/events")
public class EventController {
	private EventService eventService;
	private MessageService messageService;
	private UserService userService;

	public EventController(EventService eventService, MessageService messageService,UserService userService) {
		this.eventService = eventService;
		this.messageService= messageService;
		this.userService = userService;
	}
	
	@RequestMapping("")
	public String events(@ModelAttribute("event") Event event,HttpSession s, Model model) {
		if( s.getAttribute("id") != null ){
			long user_id =(long)s.getAttribute("id");
			User user = userService.findById(user_id);
			ArrayList<Event> userStates = eventService.findByState(user.getState());
			System.out.println("hello");
//			ArrayList<Event> notUserStates = eventService.findNotByState(user.getState());
			
			model.addAttribute("userStates", userStates);
//			model.addAttribute("notUserStates", notUserStates);
			
			ArrayList<Event> allEvents =(ArrayList<Event>)eventService.all();
			ArrayList<Event> notUserStates = new ArrayList<Event>();
			ArrayList<Event> joinedStates= new ArrayList<Event>();
			ArrayList<Event> allUserStates = new ArrayList<Event>();
			ArrayList<Event> notJoinedStates= new ArrayList<Event>();
			
			
			//this finds states that are equal to the user states
			for(int i=0; i<allEvents.size(); i++ ) {
				 if(allEvents.get(i).getState().equals(user.getState()))
					 allUserStates.add(allEvents.get(i));
			//this loops through all the user States 
				 for(int x=0; x<allUserStates.size(); x++ ) {
					 
			//this loops through all the users in the user states
					 for(int e=0;e<allUserStates.get(x).getUsers().size();e++) {
			//this checks if the user has joined an event by comparing user IDs
						 if(allUserStates.get(x).getUsers().get(e).equals(user)) 
							 joinedStates.add(allUserStates.get(x));
			//this does the opposite of ^^
//						 if( !allUserStates.get(x).getUsers().get(e)(user))
//							 notJoinedStates.add(allUserStates.get(x));
				 }
				}
				 if( !allEvents.get(i).getState().equals(user.getState()))
					 notUserStates.add(allEvents.get(i));

				
			}
			System.out.println(notJoinedStates);
			System.out.println(joinedStates);
			model.addAttribute("notJoinedEvents", notJoinedStates);
			model.addAttribute("joinedEvents", joinedStates);
			model.addAttribute("notUserStates", allEvents);
			return "dashboard";
		}else{

			return "redirect:/";
		}
	}
	
	@PostMapping("/new")
	public String create(@Valid @ModelAttribute("event") Event event, BindingResult res) {
		if(res.hasErrors()) {return "redirect:/";}
			eventService.create(event);
			return "redirect:/events";
		
	}
	
	@RequestMapping("/{id}")
	public String showEvent(@PathVariable("id") long id, Model model,@ModelAttribute("message") Message message){
		model.addAttribute("event",(Event)eventService.findById(id));
		
		return "showEvent";
	}
	
	@PostMapping("/{id}/messages/new")
	public String comment( @Valid @ModelAttribute("message") Message message, BindingResult res,@PathVariable("id") long event_id,HttpSession session,Model model) {
		System.out.println(message.getUser());
		if(res.hasErrors()) {return "redirect:/events/"+event_id;}
		message.setId(null);

		messageService.create(message);
		long user_id = (long)session.getAttribute("id");
		User u =(User)userService.findById(user_id);
		Event es = eventService.findById(event_id);
//		List<Message> msgs =es.getMessages();
//		msgs.add( message);
//		es.setMessages(msgs);
		
		model.addAttribute("commenter",u);
		message.setUser(u);
		message.setEvent(es);
		eventService.create(es);


		return "redirect:/events/"+event_id;
	}
	
	@PostMapping("/{id}/join")
	public String join(@PathVariable("id") long event_id, HttpSession session) {
		Event e = eventService.findById(event_id);
		long user_id = (long)session.getAttribute("id");
		User u =(User)userService.findById(user_id);
		
		List<User> users =e.getUsers();
		users.add(u);
		
		e.setUsers(users);
		
		 eventService.update(e);
		 return "redirect:/";
	}
}



