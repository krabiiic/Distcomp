package lab1.services;

import lab1.datalayer.DataLayer;
import lab1.datalayer.FieldChecker;
import lab1.datalayer.InMemoryDataLayer;
import lab1.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserService {

    private final DataLayer dataLayer;
    public UserService(){
        dataLayer = new InMemoryDataLayer();
    }

    public ArrayList<User> GetUsers(){
       return dataLayer.GetUsers();
    }

    public User GetUserById(long Id) throws IllegalArgumentException{
        User res = dataLayer.GetUser(Id);
        return res;
    }

    public boolean CreateUser(User user){
        if (FieldChecker.CheckUserFields(user)){
            dataLayer.CreateUser(user);
            return user.getId() != -1;
        }else
            return false;
    }

    public User DeleteUser(long Id){
        return dataLayer.DeleteUser(Id);
    }

    public User UpdateUser(User user){
        if (FieldChecker.CheckUserFields(user))
            return dataLayer.UpdateUser(user);
        else
            return null;
    }

    public DataLayer getDataLayer(){
        return dataLayer;
    }

}
