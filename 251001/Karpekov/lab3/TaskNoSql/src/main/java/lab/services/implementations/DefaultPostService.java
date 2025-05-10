package lab.services.implementations;

import jakarta.persistence.LockModeType;
import lab.repository.IssueDao;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lab.datalayer.FieldChecker;
import lab.models.Issue;
import lab.repository.PostDao;
import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.IllegalFieldDataException;
import lab.models.Post;
import lab.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public class DefaultPostService implements PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private IssueDao issueDao;

    public DefaultPostService() {
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<Post> getPosts() {
        return postDao.findAll();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Post findById(Long Id) throws DataObjectNotFoundException {
        return postDao.findById(Id).orElseThrow(() -> new DataObjectNotFoundException("Wrong field format"));
    }

    @Override
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    public Post createPost(Post post) throws IllegalFieldDataException, DataAccessException {
        if (FieldChecker.CheckPostFields(post, postDao)) {
            if (issueDao.existsById(post.getIssue().getId())) {
                post.setId(null);
                return postDao.saveAndFlush(post);
            } else {
                throw new DataObjectNotFoundException("Issue for post not found");
            }
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deletePost(Long Id) throws DataObjectNotFoundException {
        try {
            if (postDao.existsById(Id)) {
                postDao.deleteById(Id);
                postDao.flush();
            } else {
                throw new DataObjectNotFoundException("No such object to delete");
            }
        } catch (DataAccessException dataAccessException) {
            throw new DataObjectNotFoundException(dataAccessException);
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Post updatePost(Post post) {
        if (FieldChecker.CheckPostFields(post, postDao) && post.getId() > 0) {
            if (issueDao.existsById(post.getIssue().getId())) {
                return postDao.saveAndFlush(post);
            } else {
                throw new DataObjectNotFoundException("Issue for post not found");
            }
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deleteByIssue(Issue issue) throws DataObjectNotFoundException {
        if (issue != null) {
            try {
                postDao.deleteByIssue(issue);
                postDao.flush();
            } catch (DataAccessException dataAccessException) {
                throw new DataObjectNotFoundException(dataAccessException);
            }
        }
    }
}
