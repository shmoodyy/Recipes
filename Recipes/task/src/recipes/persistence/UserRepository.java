package recipes.persistence;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.business.models.user.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findUserByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);
}