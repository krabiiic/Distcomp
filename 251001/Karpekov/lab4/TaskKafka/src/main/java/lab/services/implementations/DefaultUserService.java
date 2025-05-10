package lab.services.implementations;

import jakarta.persistence.LockModeType;
import lab.datalayer.FieldChecker;
import lab.repository.UserDao;
import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.IllegalFieldDataException;
import lab.models.User;
import lab.services.IssueService;
import lab.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public class DefaultUserService implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IssueService issueService;

    public DefaultUserService() {
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public User findById(Long Id) throws DataObjectNotFoundException {
        return userDao.findById(Id).orElseThrow(() -> new DataObjectNotFoundException("User not found"));
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public User createUser(User user) throws IllegalFieldDataException, DataAccessException {
        if (FieldChecker.CheckUserFields(user, userDao)) {
            user.setId(null);
            return userDao.saveAndFlush(user);
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
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
