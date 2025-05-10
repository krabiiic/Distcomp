package lab1.datalayer;

import lab1.models.*;
import lab1.models.User;

public class FieldChecker {

    public static boolean CheckUserFields(User user){
        return user.getLogin().length() >= 2
                && user.getFirstname().length() >= 2
                && user.getLastname().length() >= 2;
    }

    public static boolean CheckIssueFields(Issue issue){
        return issue.getTitle().length() >= 2
                && issue.getContent().length() >= 4;
    }

    public static boolean CheckTagFields(Tag tag){
        return tag.getName().length() >= 2;
    }

    public static boolean CheckPostFields(Post post){
        return post.getContent().length() >= 2;
    }
}
