package da2i.payetesdettes.services;

import da2i.payetesdettes.entities.Transaction;
import da2i.payetesdettes.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    
    @Autowired
    EventService eventService;

    /**
     * Méthode qui permet de créér une nouvelle transaction
     * @param newTransaction Transaction à créer
     * @return Retourne l'instance de la transaction qui vient d'être créée
     */
    public Transaction createTransaction(Transaction newTransaction) {
    	transactionRepository.save(newTransaction);
    	eventService.addTransaction(newTransaction.getEvent(), newTransaction);
    	return newTransaction;
    }
}
