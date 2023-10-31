package da2i.payetesdettes.exceptions;

public class EventNotFoundException extends Exception {
	
	static String defaultMessage = "Événement introuvable";
	
	public EventNotFoundException () {
		super(defaultMessage);
	}

    public EventNotFoundException (String message) {
        super(message);
    }
}
