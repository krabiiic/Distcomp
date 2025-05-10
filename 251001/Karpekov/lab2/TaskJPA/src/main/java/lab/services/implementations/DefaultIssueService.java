package lab.services.implementations;

import jakarta.persistence.LockModeType;
import lab.datalayer.FieldChecker;
import lab.models.User;
import lab.repository.IssueDao;
import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.IllegalFieldDataException;
import lab.models.Issue;
import lab.repository.PostDao;
import lab.repository.UserDao;
import lab.services.IssueService;
import lab.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public class DefaultIssueService implements IssueService {

    @Autowired
    private IssueDao issueDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostService postService;

    public DefaultIssueService() {
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<Issue> getIssues() {
        return issueDao.findAll();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Issue findById(Long Id) throws DataObjectNotFoundException {
        return issueDao.findById(Id).orElseThrow(() -> new DataObjectNotFoundException("No such issue"));
    }


    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Issue createIssue(Issue issue) throws IllegalFieldDataException, DataAccessException {
        if (FieldChecker.CheckIssueFields(issue, issueDao)) {
            if (userDao.existsById(issue.getUser().getId())) {
                issue.setId(null);
                return issueDao.saveAndFlush(issue);
            } else {
                throw new DataObjectNotFoundException("User for Issue not found");
            }
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deleteIssue(Long Id) throws DataObjectNotFoundException {
        try {
            if (issueDao.existsById(Id)) {
                postService.deleteByIssue(issueDao.findById(Id).orElse(null));
                issueDao.deleteById(Id);
                issueDao.flush();
            } else {
                throw new DataObjectNotFoundException("No such object to delete");
            }
        } catch (DataAccessException dataAccessException) {
            throw new DataObjectNotFoundException(dataAccessException);
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Issue updateIssue(Issue issue) {
        if (FieldChecker.CheckIssueFields(issue, issueDao) && issue.getId() != null && issue.getId() > 0) {
            if (userDao.existsById(issue.getUser().getId())) {
                return issueDao.saveAndFlush(issue);
            } else {
                throw new DataObjectNotFoundException("User for Issue not found");
            }
        }
        else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deleteByUser(User user) throws DataObjectNotFoundException {
        if (user != null) {
            try {
                issueDao.deleteByUser(user);
                issueDao.flush();
            } catch (DataAccessException dataAccessException) {
                throw new DataObjectNotFoundException(dataAccessException);
            }
        }
    }
}
