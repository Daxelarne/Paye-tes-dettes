package da2i.payetesdettes.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "custom_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "custom_key", updatable = false, nullable = false)
    private UUID key = UUID.randomUUID();
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(columnDefinition = "boolean default false")
    private boolean isEmailConfirmed;
    
    @Column(nullable = false)
    private String password;
    
    private String login;
    
    @Column(columnDefinition = "boolean default false")
    private boolean isAdmin;
    
    private LocalDate registrationDate = LocalDate.now();
    
    private LocalDateTime lastLoginDate = LocalDateTime.now();
    
    @Transient
    private String oldPassword;
    
    @Transient
    private String newPassword;
    
    @Transient
    private String newLogin;
    
    @Transient
    private String newMail;
    
    
    
    public String getRegistrationDate() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return this.registrationDate.format(formatter);	
    }
    
    public String getLastLoginDate() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
		return this.lastLoginDate.format(formatter);	
    }


	@Override
	public String toString() {
		return login;
	}

	public User(int id, String email, boolean isEmailConfirmed, String password, String login, boolean isAdmin,
			LocalDate registrationDate, LocalDateTime lastLoginDate) {
		super();
		this.id = id;
		this.email = email;
		this.isEmailConfirmed = isEmailConfirmed;
		this.password = password;
		this.login = login;
		this.isAdmin = isAdmin;
		this.registrationDate = registrationDate;
		this.lastLoginDate = lastLoginDate;
	}
}
