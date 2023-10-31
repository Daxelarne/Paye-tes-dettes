package da2i.payetesdettes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import da2i.payetesdettes.entities.Transaction;
import da2i.payetesdettes.exceptions.EventNotFoundException;
import da2i.payetesdettes.services.EventService;
import da2i.payetesdettes.services.TransactionService;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    
    @Autowired
    EventService eventService;

    @PostMapping("/new/{eventId}")
    public RedirectView newEvent (ModelMap modelMap, @ModelAttribute Transaction transaction, @PathVariable("eventId") String eventId, BindingResult bindingResult) {
        try {
            transaction.setEvent(eventService.getDetailById(eventId));
        } catch (EventNotFoundException e) {
            return new RedirectView("/error");
        }
        Transaction transactionCreated = transactionService.createTransaction(transaction);
        return new RedirectView("/event/details/"+ transactionCreated.getEvent().getId());
    }
}
