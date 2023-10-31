package da2i.payetesdettes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import da2i.payetesdettes.entities.Comment;
import da2i.payetesdettes.repositories.CommentRepository;

@Service
public class CommentService {

	@Autowired
	CommentRepository commentRepository;
	
	/**
	 * Créer un nouveau commentaire et l'enregistre dans la base de donnée
	 * @param newComment Commentaire à créer
	 * @return Retourne l'instance du commentaire créé
	 */
	public Comment createComment(Comment newComment) {
		commentRepository.save(newComment);
		return newComment;
	}
}
