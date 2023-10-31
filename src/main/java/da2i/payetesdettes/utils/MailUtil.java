package da2i.payetesdettes.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import da2i.payetesdettes.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailUtil {
	
	@Autowired
    private JavaMailSender emailSender;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Value("${spring.mail.username}")
	private String fromAdress;

    public void sendSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(fromAdress);
        message.setTo("millevert.matthieu@gmail.com"); 
        message.setSubject("spring"); 
        message.setText("test");
        emailSender.send(message);
    }
    
    public boolean sendConfirmationEmail(User user) {
    	try {
	    	StringBuilder content = new StringBuilder();
	    	content.append("Cher "+user.getLogin()+", <br/><br/>");
	    	content.append("C'est un plaisir de vous compter parmis les utilisateurs de notre plateforme!<br/><br/>");
	    	content.append("Pour confirmer votre adresse mail, cliquez sur ce lien : ");
	    	content.append("https://localhost:8443/user/confirmEmail/"+user.getId()+"/"+user.getKey()+" <br/><br/>");
	    	content.append("L'équipe Paye Tes Dettes vous souhaite encore la bienvenue parmis nous!<br/><br/>");
	    	content.append("Matthieu et Alexandre.");
	        
	        MimeMessage mimeMessage = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
	        mimeMessage.setContent(content.toString(), "text/html");
	        
	        helper.setFrom(fromAdress);
	        helper.setTo(user.getEmail()); 
	        helper.setSubject("Bienvenue sur Paye Tes Dettes!");
			emailSender.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		} 
    }

	public boolean sendMailResetPassword(User user) {
		try {
	    	StringBuilder content = new StringBuilder();
	    	content.append("Cher "+user.getLogin()+", <br/><br/>");
	    	content.append("Vous avez effectué une demande pour réinitialiser votre mot de passe sur notre plateforme Paye Tes Dettes.<br/><br/>");
	    	content.append("Pour confirmer votre adresse mail, cliquez sur ce lien : ");
	    	content.append("https://localhost:8443/user/resetPassword/"+user.getId()+"/"+user.getKey()+" <br/><br/>");
	    	content.append("Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer cet e-mail.<br/><br/>");
	    	content.append("Matthieu et Alexandre.");
	        
	        MimeMessage mimeMessage = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
	        mimeMessage.setContent(content.toString(), "text/html");
	        
	        helper.setFrom(fromAdress);
	        helper.setTo(user.getEmail()); 
	        helper.setSubject("Réinitialisation de votre mot de passe");
			emailSender.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		} 
	}
}
