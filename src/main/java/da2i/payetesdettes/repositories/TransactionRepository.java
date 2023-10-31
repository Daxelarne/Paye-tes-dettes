package da2i.payetesdettes.repositories;

import da2i.payetesdettes.entities.Event;
import da2i.payetesdettes.entities.Transaction;
import da2i.payetesdettes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    public Optional<Transaction> findById(int id);

    public List<Transaction> findByEvent (Event event);

    public List<Transaction> findBySender(User sender);

    public List<Transaction> findByReceivers (User receiver);
}
