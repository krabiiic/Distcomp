package lab.services.implementations;

import jakarta.persistence.LockModeType;
import lab.modelDTOs.TagRequestTo;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lab.datalayer.FieldChecker;
import lab.repository.TagDao;
import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.IllegalFieldDataException;
import lab.models.Tag;
import lab.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public class DefaultTagService implements TagService {

    @Autowired
    private TagDao tagDao;

    public DefaultTagService() {
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<Tag> getTags() {
        return tagDao.findAll();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Tag findById(Long Id) throws DataObjectNotFoundException {
        return tagDao.findById(Id).orElseThrow(() -> new DataObjectNotFoundException("Tag ot found"));
    }

    @Override
    public Tag findOrCreateTag(String name){
        Optional<Tag> optionalTag = tagDao.findByName(name);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else {
            Tag tag = new Tag();
            tag.setId(null);
            tag.setName(name);
            return tagDao.saveAndFlush(tag);
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Tag createTag(Tag tag) throws IllegalFieldDataException, DataAccessException {
        if (FieldChecker.CheckTagFields(tag, tagDao)) {
            tag.setId(null);
            return tagDao.saveAndFlush(tag);
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deleteTag(Long Id) throws DataObjectNotFoundException {
        try {
            if (tagDao.existsById(Id)) {
                tagDao.deleteById(Id);
                tagDao.flush();
            } else {
                throw new DataObjectNotFoundException("No such object to delete");
            }
        } catch (DataAccessException dataAccessException) {
            throw new DataObjectNotFoundException(dataAccessException);
        }
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Tag updateTag(Tag tag) {
        if (FieldChecker.CheckTagFields(tag, tagDao) && tag.getId() > 0)
            try {
                return tagDao.saveAndFlush(tag);
            } catch (DataAccessException dataAccessException) {
                throw new DataObjectNotFoundException(dataAccessException);
            }
        else
            throw new IllegalFieldDataException("Wrong field format");
    }
}
