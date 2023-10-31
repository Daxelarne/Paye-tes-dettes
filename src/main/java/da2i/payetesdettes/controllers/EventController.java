package da2i.payetesdettes.controllers;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import da2i.payetesdettes.entities.Comment;
import da2i.payetesdettes.entities.Event;
import da2i.payetesdettes.entities.Transaction;
import da2i.payetesdettes.exceptions.EventNotFoundException;
import da2i.payetesdettes.exceptions.UserNotFoundException;
import da2i.payetesdettes.services.EventService;
import da2i.payetesdettes.services.UserService;

@Controller
@RequestMapping("/event")
public class EventController {
	
	@Autowired
	EventService eventService;
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public String event (ModelMap modelMap) {
		modelMap.addAttribute("visibilityEnum", eventService.getVisibilityValues());
		modelMap.addAttribute("stateEnum", eventService.getStateValues());
		modelMap.addAttribute("event", new Event());
		modelMap.addAttribute("publicEventList", eventService.getPublicEvents());
		modelMap.addAttribute("myEventListAsOwner", eventService.getMyEventsAsOwner());
		modelMap.addAttribute("myEventListAsParticipant", eventService.getMyEventsAsParticipant());
		return "events";
	}
	
	@GetMapping("/details/{id}")
	public String eventDetails (ModelMap modelMap, @PathVariable("id") String id) {
		try {
			Event askedEvent = eventService.getDetailById(id);
			Transaction newTransaction = new Transaction();
			newTransaction.setEvent(askedEvent);
			modelMap.addAttribute("event", askedEvent);
			for (Double amount : askedEvent.getBalance().values()) {
				System.out.println("AMOUNT  : "+ amount);
			}
			modelMap.addAttribute("newTransaction", newTransaction);
			modelMap.addAttribute("comment", new Comment());
			modelMap.addAttribute("suggestedTransactions", eventService.getSuggestedTransaction(askedEvent));
			if (eventService.checkIfUserIsAdmin(askedEvent, userService.getCurrentUserLogged())) {
				modelMap.addAttribute("visibilityEnum", eventService.getVisibilityValues());
				modelMap.addAttribute("stateEnum", eventService.getStateValues());
				return "eventDetailsAdmin";
			}
			return "eventDetails";
		} catch (EventNotFoundException e) {
			return "error";
		}
	}
	
	@GetMapping("/join/{id}")
	public String joinEvent (ModelMap modelMap, @PathVariable("id") String id) {
		try {
			eventService.joinEvent(eventService.getDetailById(id), userService.getCurrentUserLogged());
			return eventDetails(modelMap, id);
		} catch (EventNotFoundException e) {
			return "error";
		}
	}
	
	@GetMapping("/leave/{id}")
	public String leaveEvent (ModelMap modelMap, @PathVariable("id") String id) {
		try {
			eventService.leaveEvent(eventService.getDetailById(id), userService.getCurrentUserLogged());
			return eventDetails(modelMap, id);
		} catch (EventNotFoundException e) {
			return "error";
		}
	}
	
	@GetMapping("/delete/{id}")
	public RedirectView deleteEvent (@PathVariable("id") String id) {
		try {
			eventService.deleteEventById(id);
			return new RedirectView("/event");
		} catch (EventNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error");
		}
		
	}
	
	@GetMapping("/manageMembers/{eventId}")
	public String manageMembers (ModelMap modelMap, @PathVariable("eventId") String eventId) throws IOException {
		Event askedEvent;
		try {
			askedEvent = eventService.getDetailById(eventId);
			modelMap.addAttribute("event", askedEvent);
			return "manageMembers";
		} catch (EventNotFoundException e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	@GetMapping("/demote/{eventId}/{userId}")
	public RedirectView demoteUser (@PathVariable("eventId") String eventId, @PathVariable("userId") String userId) {
		try {
			eventService.demoteUser(eventService.getDetailById(eventId), userService.getUserById(userId));
			return new RedirectView("/event/manageMembers/" + eventId);
		} catch (EventNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error");
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error");
		}
	}
	
	@GetMapping("/promote/{eventId}/{userId}")
	public RedirectView promoteUser (@PathVariable("eventId") String eventId, @PathVariable("userId") String userId) {
		try {
			eventService.promoteUser(eventService.getDetailById(eventId), userService.getUserById(userId));
			return new RedirectView("/event/manageMembers/" + eventId);
		} catch (EventNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error");
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error");
		}
	}
	
	@GetMapping("/kick/{eventId}/{userId}")
	public RedirectView kickUser (@PathVariable("eventId") String eventId, @PathVariable("userId") String userId) {
		try {
			eventService.kickUser(eventService.getDetailById(eventId), userService.getUserById(userId));
			return new RedirectView("/event/manageMembers/" + eventId);
		} catch (EventNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error");
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error");
		}
	}
	
	@GetMapping("/add/{eventId}/{userEmail}")
	public RedirectView addUser (@PathVariable("eventId") String eventId, @PathVariable("userEmail") String userEmail) {
		try {
			eventService.addUser(eventService.getDetailById(eventId), userService.getUserByEmail(userEmail));
			return new RedirectView("/event/manageMembers/" + eventId + "?successAddingByEmail=" + userEmail);
		} catch (EventNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error");
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/event/manageMembers/" + eventId + "?error=failedToAdd");
		}
	}
	
	@PostMapping("/new")
    public RedirectView newEvent (ModelMap modelMap, @ModelAttribute Event event, BindingResult bindingResult) {
		Event createdEvent = eventService.createEvent(event);
        return new RedirectView("/event/details/"+ createdEvent.getId());
    }
	
	@PostMapping("/update")
    public RedirectView updateEvent (ModelMap modelMap, @ModelAttribute Event event, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			System.out.println(Arrays.toString(bindingResult.getAllErrors().toArray()));
			return new RedirectView("/error");
		}
		eventService.updateEvent(event);
        return new RedirectView("/event/details/"+event.getId());
    }
	
	
	@PostMapping("/uploadImg/{eventId}")
	public RedirectView uploadImg (@PathVariable("eventId") String eventId, @RequestParam("file") MultipartFile multipartFile) {
		
		try {
			eventService.uploadImg(eventId, multipartFile);
			return new RedirectView("/event/details/" + eventId);
		} catch (Exception e) {
			e.printStackTrace();
			return new RedirectView("/error");
		} 
	}
	
	@GetMapping("/deleteImg/{eventId}/{imageName}")
	public RedirectView deleteImg (@PathVariable("eventId") String eventId, @PathVariable("imageName") String imageName) {
		
		try {
			eventService.deleteImg(eventId, imageName);
			return new RedirectView("/event/details/" + eventId);
		} catch (Exception e) {
			e.printStackTrace();
			return new RedirectView("/error");
		} 
	}
	
	@GetMapping("/favImg/{eventId}/{imageName}")
	public RedirectView favImg (@PathVariable("eventId") String eventId, @PathVariable("imageName") String imageName) {
		
		try {
			eventService.favImg(eventId, imageName);
			return new RedirectView("/event/details/" + eventId);
		} catch (Exception e) {
			e.printStackTrace();
			return new RedirectView("/error");
		} 
	}
	@PostMapping("/search")
	public String search (ModelMap modelMap, @RequestParam String keyword) {
		modelMap.addAttribute("resultsEvents", eventService.searchByTitle(keyword));
		modelMap.addAttribute("keyword", keyword);
		return "eventSearch";
	}
}