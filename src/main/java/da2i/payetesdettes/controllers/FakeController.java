package da2i.payetesdettes.controllers;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import da2i.payetesdettes.entities.Event;
import da2i.payetesdettes.entities.StateType;
import da2i.payetesdettes.entities.Transaction;
import da2i.payetesdettes.entities.User;
import da2i.payetesdettes.entities.VisibilityType;
import da2i.payetesdettes.exceptions.EmailAlreadyUsedException;
import da2i.payetesdettes.exceptions.PasswordNotSecureException;
import da2i.payetesdettes.repositories.EventRepository;
import da2i.payetesdettes.services.EventService;
import da2i.payetesdettes.services.UserService;
import da2i.payetesdettes.utils.MailUtil;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Just a controller to initialize tests accounts, only for development
 */
@Controller
public class FakeController {

	@Autowired
	EventService eventService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	MailUtil mailUtil;
	
	@GetMapping("/init")
	public void initFakeData (HttpServletResponse res) throws IOException, EmailAlreadyUsedException, PasswordNotSecureException {
		User user = new User(1, "user", true, "User1234.", "user", false, LocalDate.now(), LocalDateTime.now());
		User admin = new User(2, "admin", true, "Admin1234.", "admin", true, LocalDate.now(), LocalDateTime.now());
		User matthieu = new User(3, "millevert.matthieu@gmail.com", true, "Matthieu1.", "Matthieu", false, LocalDate.now(), LocalDateTime.now());
		User user2 = new User(4, "user2", true, "User1234.", "user2", false, LocalDate.now(), LocalDateTime.now());
		List<Transaction> transactions = new ArrayList<>();
		HashMap<User, Double> balance = new HashMap<>();
		List<User> matthieuEvent = new ArrayList<>();
		matthieuEvent.add(matthieu);
		matthieuEvent.add(user);
		
		Event event1 = new Event(1, "Matthieu public", "desc", StateType.IN_PROGRESS, VisibilityType.PUBLIC, matthieu, transactions, new ArrayList<>(),  balance, matthieuEvent, matthieuEvent, LocalDate.now(), LocalDate.now(), LocalDate.now(), null);
		userService.registrationWithoutSendingMail(user);
		userService.registrationWithoutSendingMail(admin);
		userService.registrationWithoutSendingMail(matthieu);
		userService.registrationWithoutSendingMail(user2);
		event1 = eventService.addUsersToBalance(event1, event1.getParticipants());
		eventRepository.save(event1);

		res.sendRedirect("/login");
	}
}
