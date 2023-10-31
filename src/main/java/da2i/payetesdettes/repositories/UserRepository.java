package da2i.payetesdettes.repositories;

import da2i.payetesdettes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public Optional<User> findById (int id);

    public Optional<User> findByEmail (String email);

    public Optional<User> findByEmailAndAndPassword (String email, String password);
}
