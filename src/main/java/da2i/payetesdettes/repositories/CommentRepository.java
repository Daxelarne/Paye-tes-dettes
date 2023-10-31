package da2i.payetesdettes.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import da2i.payetesdettes.entities.Comment;
import da2i.payetesdettes.entities.Event;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {

	public List<Comment> findByEvent (Event event);
	
}
