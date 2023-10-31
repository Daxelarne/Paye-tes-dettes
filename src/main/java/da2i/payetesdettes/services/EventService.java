package da2i.payetesdettes.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import da2i.payetesdettes.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import da2i.payetesdettes.entities.Event;
import da2i.payetesdettes.entities.StateType;
import da2i.payetesdettes.entities.User;
import da2i.payetesdettes.entities.VisibilityType;
import da2i.payetesdettes.exceptions.EventNotFoundException;
import da2i.payetesdettes.repositories.EventRepository;
import da2i.payetesdettes.utils.UploadDeleteFile;

@Service
public class EventService {
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	UserService userService;
	
	
	
	/**
	 * Méthode qui permet de créer un évènement dont le propriétaire est l'utilisateur connecté
	 * @param event Évènement à créér
	 * @return Retourne l'instance de l'évènement qui a était créé (Event)
	 */
	public Event createEvent (Event event) {
		User currentUser = userService.getCurrentUserLogged();
		event.setOwner(currentUser);
		event.getAdministrators().add(currentUser);
		event.getParticipants().add(currentUser);
		event.setCreationDate(LocalDate.now());
		event = addUsersToBalance(event, event.getParticipants());
		return eventRepository.save(event);
	}

	/**
	 * Méthode qui permet de récupérer tout les évènements dont l'utilisateur connecté est le propriétaire
	 * @return Retourne une liste d'évènements (List<Event>)
	 */
	public List<Event> getMyEventsAsOwner() {
		User currentUser = userService.getCurrentUserLogged();
		return eventRepository.findByOwner(currentUser);
	}
	
	/**
	 * Méthode qui permet de récupérer tout les évènements dont l'utilisateur connecté est un participant
	 * @return Retourne une liste d'évènements (List<Event>)
	 */
	public List<Event> getMyEventsAsParticipant() {
		User currentUser = userService.getCurrentUserLogged();
		return eventRepository.findByParticipants(currentUser);
	}
	
	/**
	 * Méthode qui permet de récupérer tout les évènements dont la visibilité est publique (visibility = VisibilityType.PUBLIC)
	 * @return Retourne une liste d'évènements (List<Event>)
	 */
	public List<Event> getPublicEvents() {
		return eventRepository.findByVisibility(VisibilityType.PUBLIC);
	}

	/**
	 * Méthode qui permet de retrouver un évènement grâce à son identifiant unique (id)
	 * @param id Identifiant unique de l'évènement recherché
	 * @return Retourne l'évènement correspondant (Event)
	 * @throws EventNotFoundException L'évènement est introuvable
	 */
	public Event getDetailById(String id) throws EventNotFoundException {
		Optional<Event> optEvent = eventRepository.findById(Integer.parseInt(id));
		if (optEvent.isPresent())
			return optEvent.get();
		else
			throw new EventNotFoundException();
	}

	/**
	 * Méthode qui permet de supprimer un évènement grâce à son identifiant unique (id)
	 * @param id Identifiant unique de l'évènement à supprimer
	 * @throws EventNotFoundException L'évènement est introuvable
	 */
	public void deleteEventById(String id) throws EventNotFoundException {
		Optional<Event> optEvent = eventRepository.findById(Integer.parseInt(id));
		if (optEvent.isPresent()) {
			try {
				UploadDeleteFile.deleteAllFilesAndDirectory("src/main/webapp/eventImg/" + id);
			} catch(Exception e) {
				
			}
			
			eventRepository.delete(optEvent.get());
		} else
			throw new EventNotFoundException();
		
	}

	/**
	 * Méthode qui permet de mettre à jour un évènement
	 * @param eventUpdated Nouvelle instance de l'évènement (qui contient les modifications)
	 * @return Retourne l'instance de l'évènement avec les modifications
	 */
	public Event updateEvent(Event eventUpdated) {
		Event oldEvent = eventRepository.findById(eventUpdated.getId()).get();
		eventUpdated.setBalance(oldEvent.getBalance());
		eventUpdated.setAdministrators(oldEvent.getAdministrators());
		eventUpdated.setParticipants(oldEvent.getParticipants());
		eventUpdated.setDefaultImg(oldEvent.getDefaultImg());
		eventUpdated.setTransactions(oldEvent.getTransactions());
		return eventRepository.save(eventUpdated);
	}

	/**
	 * Méthode qui permet de vérifier si un utilisateur donné est un administrateur de l'évènement donné
	 * @param event Évènement concerné par la vérification
	 * @param user Utilisateur concerné par la vérification
	 * @return Retourne vrai s'il est administrateur de cet évènement, faux sinon
	 */
	public boolean checkIfUserIsAdmin(Event event, User user) {
		if (event.getOwner().equals(user)) return true;
		else if (event.getAdministrators().contains(user)) return true;
		else if (user.isAdmin()) return true;
		else return false;
	}
	
	/**
	 * Méthode qui permet de vérifier si un utilisateur donné est un participant de l'évènement donné
	 * @param event Évènement concerné par la vérification
	 * @param user Utilisateur concerné par la vérification
	 * @return Retourne vrai s'il est participant de cet évènement, faux sinon
	 */
	public boolean checkIfUserIsParticipant(Event event, User user) {
		return event.getParticipants().contains(user);
	}
	
	/**
	 * Méthode qui permet de vérifier si un utilisateur donné est le propriétaire de l'évènement donné
	 * @param event Évènement concerné par la vérification
	 * @param user Utilisateur concerné par la vérification
	 * @return Retourne vrai s'il est propriétaire de cet évènement, faux sinon
	 */
	public boolean checkIfUserIsOwner(Event event, User user) {
		return event.getOwner().equals(user);
	}

	/**
	 * Méthode qui retourne toutes les valeurs possibles pour la visibilité d'un évènement
	 * @return Retourne une liste de valeurs possibles (List<VisibilityType>)
	 */
	public List<VisibilityType> getVisibilityValues() {
		return Arrays.asList(VisibilityType.values());
	}
	
	/**
	 * Méthode qui retourne toutes les valeurs possibles pour l'état d'un évènement
	 * @return Retourne une liste de valeurs possibles (List<StateType>)
	 */
	public List<StateType> getStateValues() {
		return Arrays.asList(StateType.values());
	}
	
	/**
	 * Méthode qui permet à un utilisateur donné de rejoindre un évènement en l'ajoutant à la liste des participants de l'évènement
	 * @param event Évenèment concerné
	 * @param user Utilisateur à ajouter
	 * @return Retourne vrai si l'utilisateur a pu être ajouté ou s'il fait déjà parti de cet évènement, faux si nous n'avons pas pu ajouter cet utilisateur à l'évènement.
	 */
	public boolean joinEvent(Event event, User user) {
		if (!checkIfUserIsParticipant(event, user)) {
			event.getParticipants().add(user);
			eventRepository.save(event);
			return true;
		}
		return true;
	}
	
	/**
	 * Méthode qui permet à un utilisateur donné de quitter un évènement en le retirant de la liste des participants de l'évènement
	 * @param event Évenèment concerné
	 * @param user Utilisateur à retirer
	 * @return Retourne vrai si l'utilisateur a pu être retiré ou s'il ne faisait déjà pas parti de cet évènement, faux si nous n'avons pas pu retirer cet utilisateur de l'évènement.
	 */
	public boolean leaveEvent(Event event, User user) {
		if (checkIfUserIsParticipant(event, user)) {
			event.getParticipants().remove(user);
			event.getAdministrators().remove(user);
			eventRepository.save(event);
			return true;
		}
		return true;
	}

	/**
	 * Méthode qui permet de rétrograder un utilisateur donné au rôle de participant pour l'évènement donné
	 * Attention, l'utilisateur connecté dois avoir les droits pour réaliser ce rétrogradage. (Propriétaire ou admin)
	 * @param event Évenèment concerné
	 * @param user Utilisateur concerné
	 * @return Retourne vrai si l'utilisateur a pu être rétrogradé participant de l'évènement ou s'il l'était déjà, faux si l'opération n'a pas pu être effectuée (manque de permissions par exemple)
	 */
	public boolean demoteUser(Event event, User user) {
		if (checkIfUserIsAdmin(event, userService.getCurrentUserLogged())) {
			if (checkIfUserIsAdmin(event, user) && !checkIfUserIsOwner(event, user)) {
				event.getAdministrators().remove(user);
				eventRepository.save(event);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Méthode qui permet de promouvoir un utilisateur donné au rôle d'administrateur pour l'évènement donné
	 * Attention, l'utilisateur connecté dois avoir les droits pour réaliser cette promotion. (Propriétaire ou admin)
	 * @param event Évenèment concerné
	 * @param user Utilisateur concerné
	 * @return Retourne vrai si l'utilisateur a pu être promu administrateur de l'évènement ou s'il l'était déjà, faux si l'opération n'a pas pu être effectuée (manque de permissions par exemple)
	 */
	public boolean promoteUser(Event event, User user) {
		if (checkIfUserIsAdmin(event, userService.getCurrentUserLogged())) {
			event.getAdministrators().add(user);
			eventRepository.save(event);
			return true;
		}
		return false;
	}
	
	/**
	 * Méthode qui permet de virer un utilisateur donné d'un évènement donné
	 * Attention, l'utilisateur connecté dois avoir les droits pour réaliser cet opération. (Propriétaire ou admin)
	 * @param event Évenèment concerné
	 * @param user Utilisateur concerné
	 * @return Retourne vrai si l'utilisateur a pu être viré de l'évènement ou s'il n'en faisait déjà pas parti, faux si l'opération n'a pas pu être effectuée (manque de permissions par exemple)
	 */
	public boolean kickUser(Event event, User user) {
		if (checkIfUserIsAdmin(event, userService.getCurrentUserLogged())) {
			if (checkIfUserIsParticipant(event, user) && !checkIfUserIsOwner(event, user)) {
				event.getParticipants().remove(user);
				if (checkIfUserIsAdmin(event, user)) {
					event.getAdministrators().remove(user);
				}
				eventRepository.save(event);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Méthode qui permet d'ajouter un utilisateur donné à un évènement donné
	 * Attention, l'utilisateur connecté dois avoir les droits pour réaliser cet ajout. (Propriétaire ou admin)
	 * @param event Évenèment concerné
	 * @param user Utilisateur concerné
	 * @return Retourne vrai si l'utilisateur a pu être ajouté à l'évènement ou s'il en faisait déjà pas parti, faux si l'opération n'a pas pu être effectuée (manque de permissions par exemple)
	 */
	public boolean addUser(Event event, User user) {
		if (checkIfUserIsAdmin(event, userService.getCurrentUserLogged())) {
			if (!checkIfUserIsParticipant(event, user)) {
				event.getParticipants().add(user);
				event = addUserToBalance(event, user);
				eventRepository.save(event);
			}
			return true;
		}
		return false;
	}

	/**
	 * Méthode qui permet d'ajouter une transaction donnée à un évènement donné
	 * @param event Évènement concerné
	 * @param transaction Transaction à ajouter
	 * @return Retourne vrai si la transaction a été ajoutée, faux sinon
	 */
	public boolean addTransaction(Event event, Transaction transaction) {
		event.getTransactions().add(transaction);
		event = updateBalance(event, transaction);
		eventRepository.save(event);
		return true;
	}

	public void uploadImg(String eventId, MultipartFile multipartFile) throws IOException {
		UploadDeleteFile.saveFile("src/main/webapp/eventImg/" + eventId, multipartFile.getOriginalFilename(), multipartFile);
	}

	public void deleteImg(String eventId, String imageName) {
		UploadDeleteFile.deleteFile("src/main/webapp/eventImg/"+eventId +"/" + imageName);
		Event event = eventRepository.getReferenceById(Integer.valueOf(eventId));
		
		if (imageName.equals(event.getDefaultImg())) {
			event.setDefaultImg(null);
			eventRepository.save(event);
		}
	}
	
	/**
	 * Méthode qui permet d'ajouter un utilisateur à la balance avec un solde nul
	 * @param event Évènement concerné
	 * @param user Utilisateur à ajouter
	 * @return Retourne une instance de l'évènement (Event) avec l'utilisateur ajouté à la balance
	 */
	public Event addUserToBalance(Event event, User user) {
		event.getBalance().put(user, 0.0);
		return event;
	}
	
	/**
	 * Méthode qui permet d'ajouter une liste d'utilisateur à la balance avec un solde nul
	 * @param event Évènement concerné
	 * @param users Liste d'utilisateurs à ajouter
	 * @return Retourne une instance de l'évènement (Event) avec les utilisateurs ajoutés à la balance
	 */
	public Event addUsersToBalance(Event event, List<User> users) {
		Map<User, Double> balance = event.getBalance();
		for (User user : users) {
			balance.put(user, 0.0);
		}
		event.setBalance(balance);
		return event;
	}
	
	/**
	 * Méthode qui permet de mettre à jour la balance d'un évènement avec la transaction donnée
	 * @param event Évènement concerné
	 * @param transaction Transaction qui fait l'objet de la mise à jour
	 * @return Retourne une instance de l'évènement (Event) avec la balance mise à jour
	 */
	public Event updateBalance(Event event, Transaction transaction) {
		Map<User, Double> balance = event.getBalance();
		
		balance.put(transaction.getSender(), balance.get(transaction.getSender()) + transaction.getAmount());
		
		Double amountPerReceiver = transaction.getAmount() / transaction.getReceivers().size();
		
		for (User receiver : transaction.getReceivers()) {
			balance.put(receiver, balance.get(receiver) - amountPerReceiver);
		}
		
		event.setBalance(balance);
		return event;
	}

	/**
	 * Méthode permettant de lister tous les utilisateurs avec une balance positive pour l'évènement passé en paramètre
	 * @param event Évènement concerné
	 * @return Retourne une liste d'utilisateurs dont la balance est positive pour cet évènement
	 */
	public List<User> getParticipantsWithPositiveBalance(Event event) {
		List<User> participantsWithPositiveBalance = new ArrayList<>();
		for (User participant : event.getParticipants()) {
			if (event.getBalance().get(participant) > 0.0) {
				participantsWithPositiveBalance.add(participant);
			}
		}
		return participantsWithPositiveBalance;
	}

	/**
	 * Méthode permettant de lister tous les utilisateurs avec une balance négative pour l'évènement passé en paramètre
	 * @param event Évènement concerné
	 * @return Retourne une liste d'utilisateurs dont la balance est négative pour cet évènement
	 */
	public List<User> getParticipantsWithNegativeBalance (Event event) {
		List<User> participantsWithNegativeBalance = new ArrayList<>();
		for (User participant : event.getParticipants()) {
			if (event.getBalance().get(participant) < 0.0) {
				participantsWithNegativeBalance.add(participant);
			}
		}
		return participantsWithNegativeBalance;
	}

	/**
	 * Méthode permettant de générer une liste de transactions qui permettront d'équilibrer les comptes
	 * entre tous les utilisateurs de l'évènement
	 * @param event Évènement concerné
	 * @return Retourne une liste de transactions suggérées (List<SuggestedTransaction>)
	 */
	public List<SuggestedTransaction> getSuggestedTransaction (Event event) {
		Event copyEvent = event.partialCopyEvent();
		List<SuggestedTransaction> suggestedTransactions = new ArrayList<>();
		for (User userWithPositiveBalance : getParticipantsWithPositiveBalance(copyEvent)) {
			Double amount = copyEvent.getBalance().get(userWithPositiveBalance);
			int index = 0;
			while (amount > 0.0) {
				double amountNeededForTransaction;
				User userWithNegativeBalance = getParticipantsWithNegativeBalance(copyEvent).get(index);
				double balanceOfUserWithNegativeBalance = Math.abs(copyEvent.getBalance().get(userWithNegativeBalance));
				if (amount >= balanceOfUserWithNegativeBalance) { // 60 > 50
					amountNeededForTransaction = balanceOfUserWithNegativeBalance;
					copyEvent.getBalance().replace(userWithPositiveBalance, amount - balanceOfUserWithNegativeBalance);
					amount -= balanceOfUserWithNegativeBalance;
					balanceOfUserWithNegativeBalance = 0.0;
					copyEvent.getBalance().replace(userWithNegativeBalance, 0.0);
				} else {
					amountNeededForTransaction = amount;
					copyEvent.getBalance().replace(userWithNegativeBalance, copyEvent.getBalance().get(userWithNegativeBalance) + amount); // 50 - 16 // -50 + 16
					balanceOfUserWithNegativeBalance -= amount;
					amount = 0.0;
					copyEvent.getBalance().replace(userWithPositiveBalance, 0.0);
				}
				suggestedTransactions.add(new SuggestedTransaction(userWithNegativeBalance, userWithPositiveBalance, amountNeededForTransaction));
			}
		}
		eventRepository.save(event);
		return suggestedTransactions;
	}

	/**
	 * Méthode permettant de mettre une image en favori pour un évènement
	 * @param eventId Identifiant unique de l'évènement
	 * @param imageName Nom de l'image à mettre en favori
	 */
	public void favImg(String eventId, String imageName) {
		Event event = eventRepository.getReferenceById(Integer.valueOf(eventId));
		if(!imageName.equals(event.getDefaultImg())) {
			event.setDefaultImg(imageName);
		} else {
			event.setDefaultImg(null);
		}
		eventRepository.save(event);
	}

	/**
	 * Méthode permettant d'effectuer une recherche d'évènements à partir d'un mot clé
	 * @param keyword Mot clé recherché
	 * @return Retourne une liste d'évènements (List<Event) dont le titre contient le mot clé
	 */
    public List<Event> searchByTitle(String keyword) {
		return eventRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
