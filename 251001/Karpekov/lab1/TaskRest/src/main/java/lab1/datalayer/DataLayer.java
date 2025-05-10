package lab1.datalayer;

import lab1.models.Issue;
import lab1.models.Post;
import lab1.models.Tag;
import lab1.models.User;

import java.util.ArrayList;

public interface DataLayer {

    public ArrayList<User> GetUsers();

    public ArrayList<Post> GetPosts();

    public ArrayList<Tag> GetTags();

    public ArrayList<Issue> GetIssues();

    public User GetUser(long Id);
    public Post GetPost(long Id);
    public Tag GetTag(long Id);
    public Issue GetIssue(long Id);

    public User CreateUser(User user);
    public Post CreatePost(Post post);
    public Tag CreateTag(Tag tag);
    public Issue CreateIssue(Issue issue);

    public User DeleteUser(long Id);
    public Post DeletePost(long Id);
    public Tag DeleteTag(long Id);
    public Issue DeleteIssue(long Id);


    public User UpdateUser(User user);
    public Post UpdatePost(Post post);
    public Tag UpdateTag(Tag tag);
    public Issue UpdateIssue(Issue issue);
}
