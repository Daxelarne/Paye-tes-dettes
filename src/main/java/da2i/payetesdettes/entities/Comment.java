package da2i.payetesdettes.entities;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Lazy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "comments")
public class Comment {
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
    private String message;
	
	@Column(nullable = false)
    private LocalDateTime date;
	
	public Comment(Event event, User sender, String message) {
		this.event=event;
		this.sender=sender;
		this.message=message;
	}
 }
