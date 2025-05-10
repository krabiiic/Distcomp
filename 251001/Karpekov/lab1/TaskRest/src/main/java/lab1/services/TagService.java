package lab1.services;

import lab1.datalayer.DataLayer;
import lab1.datalayer.FieldChecker;
import lab1.datalayer.InMemoryDataLayer;
import lab1.models.Tag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TagService {

    private final DataLayer dataLayer;
    public TagService(){
        dataLayer = new InMemoryDataLayer();
    }

    public ArrayList<Tag> GetTags(){
       return dataLayer.GetTags();
    }

    public Tag GetTagById(long Id) throws IllegalArgumentException{
        Tag res = dataLayer.GetTag(Id);
        return res;
    }

    public boolean CreateTag(Tag tag){
        if (FieldChecker.CheckTagFields(tag)){
            dataLayer.CreateTag(tag);
            return tag.getId() != -1;
        }else
            return false;
    }

    public Tag DeleteTag(long Id){
        return dataLayer.DeleteTag(Id);
    }

    public Tag UpdateTag(Tag tag){
        if (FieldChecker.CheckTagFields(tag))
            return dataLayer.UpdateTag(tag);
        else
            return null;
    }

    public DataLayer getDataLayer(){
        return dataLayer;
    }

}
