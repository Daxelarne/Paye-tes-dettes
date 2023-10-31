package da2i.payetesdettes.exceptions;

public class EmailAlreadyUsedException extends Exception {

    public EmailAlreadyUsedException (String message) {
        super(message);
    }
}
