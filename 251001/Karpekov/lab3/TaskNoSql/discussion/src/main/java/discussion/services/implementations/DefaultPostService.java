package discussion.services.implementations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import discussion.datalayer.FieldChecker;
import discussion.repository.PostDao;
import discussion.exceptions.DataObjectNotFoundException;
import discussion.exceptions.IllegalFieldDataException;
import discussion.models.Post;
import discussion.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class DefaultPostService implements PostService {

    @Autowired
    private PostDao postDao;

    public DefaultPostService() {
    }

    @Override
    public List<Post> getPosts() {
        return postDao.findAll();
    }

    @Override
    public Post findById(Long Id) throws DataObjectNotFoundException {
        return postDao.findFirstById(Id).orElseThrow(() -> new DataObjectNotFoundException("Wrong field format"));
    }

    @Override
    public Post createPost(Post post) throws IllegalFieldDataException, DataAccessException {
        if (FieldChecker.CheckPostFields(post, postDao)) {
            // Check if issue exists?
            return postDao.save(post);
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    public void deletePost(Long Id) throws DataObjectNotFoundException {
        try {
            Post toDel = postDao.findFirstById(Id).orElseThrow(() -> new DataObjectNotFoundException("No such object to delete"));
            postDao.delete(toDel);
        } catch (DataAccessException dataAccessException) {
            throw new DataObjectNotFoundException(dataAccessException);
        }
    }

    @Override
    public Post updatePost(Post post) {
        if (FieldChecker.CheckPostFields(post, postDao) && postDao.findFirstById(post.getId().getId()).isPresent()) {
            //Check if issue exists?
            return postDao.save(post);
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

//    @Override
//    public void deleteByIssue(Issue issue) throws DataObjectNotFoundException {
//        if (issue != null) {
//            try {
//                postDao.deleteByIssue(issue);
//                postDao.flush();
//            } catch (DataAccessException dataAccessException) {
//                throw new DataObjectNotFoundException(dataAccessException);
//            }
//        }
//    }
}
