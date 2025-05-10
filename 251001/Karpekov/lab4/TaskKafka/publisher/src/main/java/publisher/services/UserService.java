package publisher.services;
import publisher.exceptions.DataObjectNotFoundException;
import publisher.exceptions.IllegalFieldDataException;
import publisher.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public interface UserService {
    public List<User> getUsers();

    public User findById(Long Id) throws DataObjectNotFoundException;

    public User createUser(User user) throws IllegalFieldDataException, DataAccessException;

    public void deleteUser(Long Id) throws DataObjectNotFoundException;

    public User updateUser(User user) throws DataObjectNotFoundException, IllegalFieldDataException;
}
