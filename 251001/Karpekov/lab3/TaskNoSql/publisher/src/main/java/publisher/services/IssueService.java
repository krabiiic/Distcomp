package publisher.services;
import publisher.exceptions.DataObjectNotFoundException;
import publisher.exceptions.IllegalFieldDataException;
import publisher.models.Issue;
import publisher.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public interface IssueService {
    public List<Issue> getIssues();

    public Issue findById(Long Id) throws DataObjectNotFoundException;

    public Issue createIssue(Issue issue) throws IllegalFieldDataException, DataAccessException;

    public void deleteIssue(Long Id) throws DataObjectNotFoundException;

    public Issue updateIssue(Issue issue) throws DataObjectNotFoundException, IllegalFieldDataException;

    public void deleteByUser(User user) throws DataObjectNotFoundException;
}
