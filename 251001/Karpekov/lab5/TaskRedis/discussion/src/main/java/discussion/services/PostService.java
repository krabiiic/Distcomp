package discussion.services;

import discussion.exceptions.DataObjectNotFoundException;
import discussion.exceptions.IllegalFieldDataException;
import discussion.models.Post;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PostService {
    public List<Post> getPosts();

    public Post findById(Long Id) throws DataObjectNotFoundException;

    public Post createPost(Post post) throws IllegalFieldDataException, DataAccessException;

    public void deletePost(Long Id) throws DataObjectNotFoundException;

    public Post updatePost(Post post) throws IllegalFieldDataException, DataObjectNotFoundException;

    //public void deleteByIssue(Issue issue) throws DataObjectNotFoundException;
}
