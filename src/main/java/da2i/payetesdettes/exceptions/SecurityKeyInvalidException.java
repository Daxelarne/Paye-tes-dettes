package da2i.payetesdettes.exceptions;

public class SecurityKeyInvalidException extends Exception {
	
	static String defaultMessage = "La clé de sécurité est invalide!";
	
	public SecurityKeyInvalidException () {
		super(defaultMessage);
	}

    public SecurityKeyInvalidException (String message) {
        super(message);
    }
}
