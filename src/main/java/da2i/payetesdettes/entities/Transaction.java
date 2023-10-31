package da2i.payetesdettes.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lazy
    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;

    @Lazy
    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @Column(nullable = false)
    private Double amount;

    private String reason;
    
    private LocalDateTime date;

    @ManyToMany()
    @JoinTable( name = "users_transactions_receivers",
            joinColumns = @JoinColumn(name = "Transaction.id"),
            inverseJoinColumns = @JoinColumn(name = "User.id"))
    private List<User> receivers = new ArrayList<>();
    
    public String getDateString() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return this.date.format(formatter);	
    }
}
