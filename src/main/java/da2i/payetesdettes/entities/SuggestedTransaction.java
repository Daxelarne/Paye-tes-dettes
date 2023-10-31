package da2i.payetesdettes.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SuggestedTransaction {
    User sender;
    User receiver;
    Double amount;

    public String toString () {
        return sender.getLogin()+" dois donner "+amount+"€ à "+receiver.getLogin();
    }
}
