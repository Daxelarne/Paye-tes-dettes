package da2i.payetesdettes.exceptions;

public class UserNotFoundException extends Exception {
	
	static String defaultMessage = "Utilisateur introuvable";
	
	public UserNotFoundException () {
		super(defaultMessage);
	}

    public UserNotFoundException (String message) {
        super(message);
    }
}
