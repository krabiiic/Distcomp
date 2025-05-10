package lab1.services;

import lab1.datalayer.DataLayer;
import lab1.datalayer.FieldChecker;
import lab1.datalayer.InMemoryDataLayer;
import lab1.models.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PostService {

    private final DataLayer dataLayer;
    public PostService(){
        dataLayer = new InMemoryDataLayer();
    }

    public ArrayList<Post> GetPosts(){
       return dataLayer.GetPosts();
    }

    public Post GetPostById(long Id) throws IllegalArgumentException{
        Post res = dataLayer.GetPost(Id);
        return res;
    }

    public boolean CreatePost(Post post){
        if (FieldChecker.CheckPostFields(post)){
            dataLayer.CreatePost(post);
            return post.getId() != -1;
        }else
            return false;
    }

    public Post DeletePost(long Id){
        return dataLayer.DeletePost(Id);
    }

    public Post UpdatePost(Post post){
        if (FieldChecker.CheckPostFields(post))
            return dataLayer.UpdatePost(post);
        else
            return null;
    }

    public DataLayer getDataLayer(){
        return dataLayer;
    }

}
