package publisher.services.implementations;

import jakarta.persistence.LockModeType;
import org.apache.kafka.clients.KafkaClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import publisher.datalayer.FieldChecker;
import publisher.modelDTOs.PostResponseTo;
import publisher.models.Post;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import publisher.services.PostService;

import java.util.List;
import java.util.Objects;

@Component
public class DefaultIssueService implements IssueService {

    @Autowired
    private IssueDao issueDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostService postService;

    @Autowired
    CacheManager cacheManager;

    public DefaultIssueService() {
    }

    @Override
    public List<Issue> getIssues() {
        return issueDao.findAll();
    }

    @Override
    //@Cacheable(value = "issues", key = "#p0")
    public Issue findById(Long Id) throws DataObjectNotFoundException {
        return issueDao.findById(Id).orElseThrow(() -> new DataObjectNotFoundException("No such issue"));
    }


    @Override
    //@CachePut(value = "issues", key = "#issue.id")
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
    //@CacheEvict(value = "issues", key = "#p0")
    public void deleteIssue(Long Id) throws DataObjectNotFoundException {
        try {
            if (issueDao.existsById(Id)) {
                for (PostResponseTo post : postService.getPosts().toIterable()){
                    if (post.getIssueId() == Id) {
                        postService.deletePost(post.getId());
                    }
                }
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
    //@CachePut(value = "issues", key = "#issue.id")
    public Issue updateIssue(Issue issue) {
        if (FieldChecker.CheckIssueFields(issue, issueDao) && issue.getId() != null) {
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
                List<Issue> deleted = issueDao.deleteByUser(user);
                for (Issue d : deleted) {
                    Objects.requireNonNull(cacheManager.getCache("issues")).evict(d.getId());
                }
                issueDao.flush();
            } catch (DataAccessException dataAccessException) {
                throw new DataObjectNotFoundException(dataAccessException);
            }
        }
    }
}
