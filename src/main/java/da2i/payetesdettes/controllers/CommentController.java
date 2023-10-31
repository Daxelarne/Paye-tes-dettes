package da2i.payetesdettes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import da2i.payetesdettes.entities.Comment;
import da2i.payetesdettes.exceptions.EventNotFoundException;
import da2i.payetesdettes.repositories.CommentRepository;
import da2i.payetesdettes.services.CommentService;
import da2i.payetesdettes.services.EventService;
import da2i.payetesdettes.services.UserService;

@Controller
@RequestMapping("/comment")
public class CommentController {

	@Autowired
    CommentService commentService;
	
	@Autowired
	CommentRepository comRep;
	
	@Autowired
	EventService eventService;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/new/{eventId}")
    public RedirectView newEvent (ModelMap modelMap, @ModelAttribute Comment comment, @PathVariable("eventId") String eventId, BindingResult bindingResult) {
		
		try {
			
			eventService.getDetailById(eventId).getComments().add(comment);
			
			Comment commentCreated = commentService.createComment(comment);
			
			
			
			
			
		} catch (EventNotFoundException e) {
			e.printStackTrace();
		}
		
        return new RedirectView("/event/details/"+ eventId);
    }
}
