package da2i.payetesdettes.controllers;

import da2i.payetesdettes.entities.UserUploadFile;
import da2i.payetesdettes.exceptions.UserNotFoundException;
import da2i.payetesdettes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import da2i.payetesdettes.entities.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {

	@Autowired
	UserService userService;

	@GetMapping("/forgotPassword")
	public String forgotPassword (ModelMap modelMap) {
		modelMap.addAttribute("user", new User());
		return "forgotPassword";
	}

	@GetMapping("/login")
	public String login (ModelMap modelMap) {
		modelMap.addAttribute("user", new User());
		return "login";
	}

	@GetMapping("/register")
	public String register (ModelMap modelMap) {
		modelMap.addAttribute("user", new User());
		return "register";
	}

	@GetMapping("/error")
	public String error (ModelMap modelMap) {
		return "error";
	}

	@GetMapping("/")
	public RedirectView origin (ModelMap modelMap) {
		return new RedirectView("/event");
	}

	@GetMapping("/index")
	public RedirectView index (ModelMap modelMap) {
		return new RedirectView("/event");
	}

	@GetMapping("/profile")
	public String profile (ModelMap modelMap) {
		modelMap.addAttribute("user", userService.getCurrentUserLogged());
		modelMap.addAttribute("userUploadFile", new UserUploadFile());
		return "profile";
	}

	@GetMapping("/profile/{userId}")
	public String profile (ModelMap modelMap, @PathVariable("userId") String userId) {
		try {
			modelMap.addAttribute("user", userService.getUserById(userId));
			modelMap.addAttribute("userUploadFile", new UserUploadFile());
			return "publicProfile";
			} catch (UserNotFoundException e ) {
				return "error";
			}
	}
}
