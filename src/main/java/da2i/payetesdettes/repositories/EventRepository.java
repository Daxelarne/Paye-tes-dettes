package da2i.payetesdettes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import da2i.payetesdettes.entities.Event;
import da2i.payetesdettes.entities.StateType;
import da2i.payetesdettes.entities.User;
import da2i.payetesdettes.entities.VisibilityType;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {

    public Optional<Event> findById (int id);

    public List<Event> findByOwner (User owner);
    
    public List<Event> findByState (StateType stateType);
    
    public List<Event> findByVisibility (VisibilityType visibilityType);
    
    public List<Event> findByParticipants (User user);

    public List<Event> findByTitleContainingIgnoreCase (String keyword);
}
