package da2i.payetesdettes.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import da2i.payetesdettes.entities.User;
import da2i.payetesdettes.entities.UserUploadFile;
import da2i.payetesdettes.exceptions.AuthentificationFailedException;
import da2i.payetesdettes.exceptions.EmailAlreadyUsedException;
import da2i.payetesdettes.exceptions.PasswordNotSecureException;
import da2i.payetesdettes.exceptions.SecurityKeyInvalidException;
import da2i.payetesdettes.exceptions.UserNotFoundException;
import da2i.payetesdettes.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")

    public RedirectView register (ModelMap modelMap, @ModelAttribute User user, BindingResult bindingResult) {
        try {
            userService.registration(user);
            return new RedirectView("/login?success=welcomeEmailSent");
        } catch (EmailAlreadyUsedException e) {
        	 return new RedirectView("/register?error=emailAlreadyUsed");
        } catch (PasswordNotSecureException e) {
        	 return new RedirectView("/register?error=passwordNotSecure");

        }
    }

    @PostMapping("/login")
    public String login (ModelMap modelMap, @ModelAttribute User user, BindingResult bindingResult) {
        return "index";
    }
    
    @PostMapping("/edit")
    public RedirectView profile (ModelMap modelMap, @ModelAttribute User user, BindingResult bindingResult) {
    	try {
    		userService.editUser(user);
			return new RedirectView("/profile");
    	} catch(EmailAlreadyUsedException e) {
    		return new RedirectView("/profile?error=emailAlreadyUsed");
    	} catch(AuthentificationFailedException e) {
    		return new RedirectView("/profile?error=passwordNotTheSame");
    	} catch(PasswordNotSecureException e) {
    		return new RedirectView("/profile?error=passwordNotSecure");
    	}
    }
    
    @PostMapping("/uploadFile")
    public RedirectView profilePicture (ModelMap modelMap, @RequestParam("file") MultipartFile multipartFile, @ModelAttribute UserUploadFile userUploadFile, BindingResult bindingResult) {
    	try {
    		userUploadFile.setFile(multipartFile);
			userService.uploadProfilImage(userUploadFile);
			return new RedirectView("/profile");
		} catch (IOException e) {
			return new RedirectView("/profile?error=errorUpload");
		}
    }
    

    
    @GetMapping("/deleteProfilePicture/{id}")
    public RedirectView deleteProfilePicture (@PathVariable("id") String id) {
		userService.deleteProfilePicture(id);
		return new RedirectView("/profile");
	}
    
    @GetMapping("/confirmEmail/{userId}/{userKey}")
    public RedirectView confirmEmail (@PathVariable("userId") String userId, @PathVariable("userKey") String userKey) {
    	try {
    		userService.confirmEmail(userId, userKey);
    		return new RedirectView("/login?success=emailConfirmed");
    	} catch (UserNotFoundException e) {
    		e.printStackTrace();
    		return new RedirectView("/error?error=userNotFound");
    	} catch (SecurityKeyInvalidException e) {
			e.printStackTrace();
			return new RedirectView("/error?error=confirmEmailInvalidSecurityKey");
		}
    }
    
    @PostMapping("/sendMailResetPassword")
    public RedirectView sendMailResetPassword (@RequestParam(name="email") String userEmail) {
    	try {
    		if (userService.sendMailResetPassword(userEmail))
    			return new RedirectView("/login?success=resetEmailSent");
    		else
    			return new RedirectView("/login");
    	} catch (UserNotFoundException e) {
    		return new RedirectView("/forgotPassword?error=userNotFound");
    	}
    }
   
    @GetMapping("/resetPassword/{userId}/{userKey}")
    public String resetPassword (ModelMap modelMap, @PathVariable("userId") String userId, @PathVariable("userKey") String userKey) {
    	try {
    		modelMap.addAttribute("userFound", userService.getUserById(userId));
    		return "resetPassword";
    	} catch (UserNotFoundException e) {
    		return "error";
    	}
    }
    
    @PostMapping("/resetPassword")
    public RedirectView resetPassword (ModelMap modelMap, @ModelAttribute User user, BindingResult bindingResult) {
    	try {
			userService.changePassword(user);
			return new RedirectView("/login?success=changePassword");
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new RedirectView("/error?error=userNotFound");
		} catch (PasswordNotSecureException e) {
			e.printStackTrace();
			return new RedirectView("/resetPassword?error=passwordNotSecure");
		} catch (SecurityKeyInvalidException e) {
			e.printStackTrace();
			return new RedirectView("/resetPassword?error=invalidSecurityKey");
		}
    	
    }
	

}
