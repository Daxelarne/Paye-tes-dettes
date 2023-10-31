package da2i.payetesdettes.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import da2i.payetesdettes.authentification.CustomUserDetails;
import da2i.payetesdettes.entities.User;
import da2i.payetesdettes.entities.UserUploadFile;
import da2i.payetesdettes.exceptions.AuthentificationFailedException;
import da2i.payetesdettes.exceptions.EmailAlreadyUsedException;
import da2i.payetesdettes.exceptions.PasswordNotSecureException;
import da2i.payetesdettes.exceptions.SecurityKeyInvalidException;
import da2i.payetesdettes.exceptions.UserNotFoundException;
import da2i.payetesdettes.repositories.UserRepository;
import da2i.payetesdettes.utils.MailUtil;
import da2i.payetesdettes.utils.UploadDeleteFile;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
	MailUtil mailUtil;
    
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    /**
     * Méthode qui permet d'enregistrer un nouvel utilisateur
     * @param user Utilisateur à ajouter
     * @return Retourne l'instance de l'utilisateur qui a été ajouté (User)
     * @throws EmailAlreadyUsedException L'email de ce nouvel utilisateur est déjà utilisé et ne peux pas être réutilisée à nouveau
     * @throws PasswordNotSecureException Le mot de passe ne réponds pas aux critères de sécurité à respecter
     */
    public User registration(User user) throws EmailAlreadyUsedException, PasswordNotSecureException {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
        	if (passwordIsConform(user.getPassword())) {
        		user.setPassword(passwordEncoder.encode(user.getPassword()));
            	user.setRegistrationDate(LocalDate.now());
            	User userSaved = userRepository.save(user);
            	mailUtil.sendConfirmationEmail(userSaved);
            	return userSaved;
        	} else throw new PasswordNotSecureException("Le mot de passe n'est pas sécurisé");
        } else
        	throw new EmailAlreadyUsedException("Inscription impossible, cet email est déjà utilisé");
    }

	/**
	 * Méthode dédiée aux développements. Méthode permettant de s'inscrire sans devoir activer son compte via un mail de bienvenue
	 * @param user Utilisateur à inscrire
	 * @return Retourne l'instance de l'utilisateur inscris
	 * @throws EmailAlreadyUsedException L'email de ce nouvel utilisateur est déjà utilisé et ne peux pas être réutilisée à nouveau
	 * @throws PasswordNotSecureException Le mot de passe ne réponds pas aux critères de sécurité à respecter
	 */
	public User registrationWithoutSendingMail(User user) throws EmailAlreadyUsedException, PasswordNotSecureException {
		if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
			if (passwordIsConform(user.getPassword())) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setRegistrationDate(LocalDate.now());
				user.setEmailConfirmed(true);
				return userRepository.save(user);
			} else throw new PasswordNotSecureException("Le mot de passe n'est pas sécurisé");
		} else
			throw new EmailAlreadyUsedException("Inscription impossible, cet email est déjà utilisé");
	}
    
    /**
     * Méthode qui permet d'obtenir l'utilisateur actuellement connecté
     * @return Retourne l'instance de l'utilisateur connecté (User)
     */
    public User getCurrentUserLogged() {
    	CustomUserDetails currentLoggedUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return currentLoggedUserDetails.getUser();
    }
    
    /**
     * Méthode qui permet de mettre à jour la date de dernière connexion d'un utilisateur donné à la date actuelle
     * @param user Utilisateur concerné
     * @return Retourne l'instance de l'utilisateur concerné avec la mise à jour
     */
    public User updateLastLoginDate (User user) {
    	user.setLastLoginDate(LocalDateTime.now());
    	return userRepository.save(user);
    }

    
    /**
     * Modifie l'utilisateur. Utilisé dans la page de profil pour changer les informations de connexions
     * 
     * @param user Utilisateur concerné
     * @throws EmailAlreadyUsedException Cette adresse e-mail est déjà utilisé et ne peux pas être réutilisée à nouveau
     * @throws AuthentificationFailedException
     * @throws PasswordNotSecureException Le mot de passe ne réponds pas aux critères de sécurité à respecter
     */
	public void editUser(User user) throws EmailAlreadyUsedException, AuthentificationFailedException, PasswordNotSecureException {
		if(userRepository.findByEmail(user.getEmail()).isEmpty()) {
			Optional<User> oldOptUser = userRepository.findById( user.getId() );
			User oldUser;
			
			if(oldOptUser.isEmpty()) {
				throw new AuthentificationFailedException("Erreur lors de la modification du profil");
			} else {
				oldUser = oldOptUser.get();
				
				//Vérifie si les mdps sont les même pour éviter le vol de compte
				if(! passwordEncoder.matches(user.getOldPassword(), oldUser.getPassword())  ) throw new AuthentificationFailedException("Les mots de passes ne correspondent pas");
				
				//Change l'objet oldUser champ par champ
				if(user.getNewLogin() != null && !user.getNewLogin().equals("")) oldUser.setLogin(user.getNewLogin());
				
				if(user.getNewMail() != null && !user.getNewMail().equals("")) oldUser.setEmail(user.getNewMail());
				
				if(user.getNewPassword() != null && !user.getNewPassword().equals("")) {
					if(passwordIsConform(user.getNewPassword())) {
						oldUser.setPassword(passwordEncoder.encode(user.getNewPassword()));
					} else throw new PasswordNotSecureException("Le mot de passe n'est pas sécurisé");
					
				}
				
				
				//Met à jour user dans la base
				userRepository.save(oldUser);
				
				
				//Met à jour user dans la session spring
				Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				CustomUserDetails storedUser = (CustomUserDetails) auth;
				
				storedUser.updateUser(oldUser);
			}
			
			
		} else {
			throw new EmailAlreadyUsedException("Modification impossible, cet email est déjà utilisé");
		}
		
	}
	
	/** Méthode qui vérifie que le mot de passe passé en paramètre correspond aux prérequis
	 * 
	 * @param password Mot de passe à vérifier
	 * @return Retourne vrai si les critères sont respectés, faux sinon
	 */
	private boolean passwordIsConform(String password) {
		if(password.length()>=8
			&& password.matches(".*\\d.*")
			&& password.matches(".*[A-Z].*")
			&& password.matches(".*[^a-zA-Z0-9\\d\\s:].*")
				
				) return true;
		
		return false;
	}

	/** Méthode permettant d'upload une image de profil depuis la page de profil
	 * 
	 * @param userUploadFile
	 * @throws IOException
	 */
	public void uploadProfilImage(UserUploadFile userUploadFile) throws IOException {
		UploadDeleteFile.saveFile("src/main/webapp/profileImg/" + userUploadFile.getId(), userUploadFile.getFile().getName(), userUploadFile.getFile());
	}
	
	

	/** Méthode permettant de supprimer l'image de profil depuis la page de profil
	 * 
	 * @param id Identifiant unique de l'utilisateur concerné
	 */
	public void deleteProfilePicture(String id)  {
		try {
			File f = new File("src/main/webapp/profileImg/" + id);
			if(f.exists()) {
				UploadDeleteFile.deleteAllFilesAndDirectory("src/main/webapp/profileImg/" + id);
			}
		} catch(IOException e) {
			// La photo n'existe pas, aucun traitement à faire
		}
		
		
	}
    
    /**
     * Méthode qui permet d'obtenir un utilisateur grâce à son identifiant unique (id)
     * @param userId Identifiant unique (id) de l'utilisateur recherché
     * @return Retourne l'instance de l'utilisateur trouvé
     * @throws UserNotFoundException L'utilisateur n'a pas pu être retrouvé
     */
    public User getUserById(String userId) throws UserNotFoundException {
    	Optional<User> optUser = userRepository.findById(Integer.parseInt(userId));
    	if (optUser.isPresent()) {
    		return optUser.get();
    	} else {
    		throw new UserNotFoundException();
    	}
    }
    
    /**
     * Méthode qui permet d'obtenir un utilisateur grâce à son adresse e-mail
     * @param userEmail Adresse e-mail de l'utilisateur recherché
     * @return Retourne l'instance de l'utilisateur trouvé
     * @throws UserNotFoundException L'utilisateur n'a pas pu être retrouvé
     */
    public User getUserByEmail(String userEmail) throws UserNotFoundException {
    	Optional<User> optUser = userRepository.findByEmail(userEmail);
    	if (optUser.isPresent()) {
    		return optUser.get();
    	} else {
    		throw new UserNotFoundException();
    	}
    }
    
    /**
     * Méthode qui permet d'obtenir tout les utilisateurs enregistrés
     * @return Retourne une liste d'utilisateurs (List<User>)
     */
    public List<User> getAllUsers() {
    	return userRepository.findAll();
    }

	public boolean confirmEmail(String userId, String userKey) throws UserNotFoundException, SecurityKeyInvalidException {
		Optional<User> optUserInDatabase = userRepository.findById(Integer.parseInt(userId));
		if (optUserInDatabase.isEmpty())
			throw new UserNotFoundException();
		User userInDatabase = optUserInDatabase.get();
		if (userInDatabase.getKey().equals(userKey))
			throw new SecurityKeyInvalidException();
		
		userInDatabase.setEmailConfirmed(true);
		userRepository.save(userInDatabase);
		return true;
	}

	/**
	 * Méthode permettant d'envoyer le mail de réinitialisation de mot de passe à un utilisateur
	 * @param userEmail Adresse e-mail de l'utilisateur
	 * @return Retourne vrai si le mail a pu être envoyé, faux sinon
	 * @throws UserNotFoundException L'utilisateur n'a pas pu être retrouvé
	 */
	public boolean sendMailResetPassword (String userEmail) throws UserNotFoundException {
		User user = getUserByEmail(userEmail);
		return mailUtil.sendMailResetPassword(user);
	}

	/**
	 * Méthode permettant de changer le mot de passe d'un utilisateur
	 * @param user Utilisateur concerné
	 * @return Retourne vrai si le mot de passe a pu être changé, faux sinon
	 * @throws UserNotFoundException L'utilisateur n'a pas pu être retrouvé
	 * @throws PasswordNotSecureException Le mot de passe ne réponds pas aux critères de sécurité à respecter
	 * @throws SecurityKeyInvalidException La clé de sécurité n'est pas valide
	 */
	public boolean changePassword(User user) throws UserNotFoundException, PasswordNotSecureException, SecurityKeyInvalidException {
		Optional<User> optUserInDatabase = userRepository.findById(user.getId());
		if (optUserInDatabase.isEmpty())
			throw new UserNotFoundException();
		User userInDatabase = optUserInDatabase.get();
		if (userInDatabase.getKey().equals(user.getKey()))
			throw new SecurityKeyInvalidException();
		
		if (passwordIsConform(user.getNewPassword())) {
			userInDatabase.setPassword(passwordEncoder.encode(user.getNewPassword()));
			userRepository.save(userInDatabase);
			return true;
		} else throw new PasswordNotSecureException("Le mot de passe n'est pas sécurisé");
		
	}
}
