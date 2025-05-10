package publisher.services.implementations;

import jakarta.persistence.LockModeType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import publisher.datalayer.FieldChecker;
import publisher.repository.UserDao;
import publisher.exceptions.DataObjectNotFoundException;
import publisher.exceptions.IllegalFieldDataException;
import publisher.models.User;
import publisher.services.IssueService;
import publisher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DefaultUserService implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IssueService issueService;

    public DefaultUserService() {
    }

    @Override
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @Override
    @Cacheable(value = "users", key = "#p0")
    public User findById(Long Id) throws DataObjectNotFoundException {
        return userDao.findById(Id).orElseThrow(() -> new DataObjectNotFoundException("User not found"));
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    public User createUser(User user) throws IllegalFieldDataException, DataAccessException {
        if (FieldChecker.CheckUserFields(user, userDao)) {
            user.setId(null);
            return userDao.saveAndFlush(user);
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    @CacheEvict(value = "users", key = "#p0")
    public void deleteUser(Long Id) throws DataObjectNotFoundException {
        try {
            if (userDao.existsById(Id)) {
                issueService.deleteByUser(userDao.findById(Id).orElse(null));
                userDao.deleteById(Id);
                userDao.flush();
            } else {
                throw new DataObjectNotFoundException("No such object to delete");
            }
        } catch (DataAccessException dataAccessException) {
            throw new DataObjectNotFoundException(dataAccessException);
        }
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        if (FieldChecker.CheckUserFields(user, userDao) && user.getId() > 0)
            try {
                return userDao.saveAndFlush(user);
            } catch (DataAccessException dataAccessException) {
                throw new DataObjectNotFoundException(dataAccessException);
            }
        else
            throw new IllegalFieldDataException("Wrong field format");
    }
}
