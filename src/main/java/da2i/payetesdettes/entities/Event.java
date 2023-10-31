package da2i.payetesdettes.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    private StateType state;
    
    private VisibilityType visibility;
    
    @Lazy
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    
    @OneToMany
    private List<Transaction> transactions = new ArrayList<>();
    
    @OneToMany
    private List<Comment> comments = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "EVENT_USER", joinColumns = @JoinColumn(name = "EVENT_ID"))
    @MapKeyJoinColumn(name = "USER_ID")
    @Column(name = "USER_DOUBLE")
    private Map<User, Double> balance = new HashMap<>();
    
    @ManyToMany()
    @JoinTable( name = "users_events_administrators",
    joinColumns = @JoinColumn(name = "Event.id"),
    inverseJoinColumns = @JoinColumn(name = "User.id"))
    private List<User> administrators = new ArrayList<>();
    
    @ManyToMany()
    @JoinTable( name = "users_events_participants",
    joinColumns = @JoinColumn(name = "Event.id"),
    inverseJoinColumns = @JoinColumn(name = "User.id"))
    private List<User> participants = new ArrayList<>();
    
    private LocalDate creationDate = LocalDate.now();
    
    private LocalDate startDate = LocalDate.now();
    
    private LocalDate endDate = LocalDate.now().plusDays(1);
    
    private String defaultImg;

    public Event partialCopyEvent () {
        Event copy = new Event();
        copy.participants = this.participants;
        copy.balance = new HashMap<>(this.balance);
        return copy;
    }

    public String getCreationDate() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return this.creationDate.format(formatter);	
    }
    
    public String getStartDateString() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return this.startDate.format(formatter);	
    }
    
    public String getEndDateString() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return this.endDate.format(formatter);	
    }
}
