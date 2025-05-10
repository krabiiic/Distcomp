package lab.services;
import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.IllegalFieldDataException;
import lab.models.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public interface TagService {
    public List<Tag> getTags();

    public Tag findById(Long Id) throws DataObjectNotFoundException;

    public Tag createTag(Tag tag) throws IllegalFieldDataException, DataAccessException;

    public Tag findOrCreateTag(String name);

    public void deleteTag(Long Id) throws DataObjectNotFoundException;

    public Tag updateTag(Tag tag) throws DataObjectNotFoundException, IllegalFieldDataException;
}
