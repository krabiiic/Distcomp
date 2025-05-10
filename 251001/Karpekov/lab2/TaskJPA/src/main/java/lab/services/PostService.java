package lab.services;

import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.IllegalFieldDataException;
import lab.models.Issue;
import lab.models.Post;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public interface PostService {
    public List<Post> getPosts();

    public Post findById(Long Id) throws DataObjectNotFoundException;

    public Post createPost(Post post) throws IllegalFieldDataException, DataAccessException;

    public void deletePost(Long Id) throws DataObjectNotFoundException;

    public Post updatePost(Post post) throws IllegalFieldDataException, DataObjectNotFoundException;

    public void deleteByIssue(Issue issue) throws DataObjectNotFoundException;
}
