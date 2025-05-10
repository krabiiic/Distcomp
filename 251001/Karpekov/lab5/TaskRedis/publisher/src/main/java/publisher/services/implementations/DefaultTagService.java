package publisher.services.implementations;

import jakarta.persistence.LockModeType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import publisher.datalayer.FieldChecker;
import publisher.repository.TagDao;
import publisher.exceptions.DataObjectNotFoundException;
import publisher.exceptions.IllegalFieldDataException;
import publisher.models.Tag;
import publisher.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultTagService implements TagService {

    @Autowired
    private TagDao tagDao;

    public DefaultTagService() {
    }

    @Override
    public List<Tag> getTags() {
        return tagDao.findAll();
    }

    @Override
    @Cacheable(value = "tags", key = "#p0")
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
    @CachePut(value = "tags", key = "#tag.id")
    public Tag createTag(Tag tag) throws IllegalFieldDataException, DataAccessException {
        if (FieldChecker.CheckTagFields(tag, tagDao)) {
            tag.setId(null);
            return tagDao.saveAndFlush(tag);
        } else {
            throw new IllegalFieldDataException("Wrong field format");
        }
    }

    @Override
    @CacheEvict(value = "tags", key = "#p0")
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
    @CachePut(value = "tags", key = "#tag.id")
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
