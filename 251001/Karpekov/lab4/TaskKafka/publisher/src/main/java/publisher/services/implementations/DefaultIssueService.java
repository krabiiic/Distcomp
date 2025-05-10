package publisher.services.implementations;

import jakarta.persistence.LockModeType;
import org.apache.kafka.clients.KafkaClient;
import publisher.datalayer.FieldChecker;
import publisher.models.User;
import publisher.repository.IssueDao;
import publisher.exceptions.DataObjectNotFoundException;
import publisher.exceptions.IllegalFieldDataException;
import publisher.models.Issue;
import publisher.repository.UserDao;
import publisher.services.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DefaultIssueService implements IssueService {

    @Autowired
    private IssueDao issueDao;

    @Autowired
    private UserDao userDao;

    public DefaultIssueService() {
    }

    @Override
    public List<Issue> getIssues() {
        return issueDao.findAll();
    }

    @Override
    public Issue findById(Long Id) throws DataObjectNotFoundException {
        return issueDao.findById(Id).orElseThrow(() -> new DataObjectNotFoundException("No such issue"));
    }


    @Override
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
    public void deleteIssue(Long Id) throws DataObjectNotFoundException {
        try {
            if (issueDao.existsById(Id)) {
                //postService.deleteByIssue(issueDao.findById(Id).orElse(null));
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
