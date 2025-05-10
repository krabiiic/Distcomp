package lab.services;
import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.IllegalFieldDataException;
import lab.models.User;
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
