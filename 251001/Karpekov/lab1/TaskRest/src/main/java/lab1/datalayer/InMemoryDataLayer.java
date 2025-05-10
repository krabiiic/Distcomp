package lab1.datalayer;

import lab1.RandomString;
import lab1.models.Issue;
import lab1.models.Post;
import lab1.models.Tag;
import lab1.models.User;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class InMemoryDataLayer implements DataLayer{

    private HashMap<Long, User> users = new HashMap<>();
    private HashMap<Long, Post> posts = new HashMap<>();
    private HashMap<Long, Tag> tags = new HashMap<>();
    private HashMap<Long, Issue> issues = new HashMap<>();

    public InMemoryDataLayer(){
        User user = new User();
        RandomString gen = new RandomString(8, ThreadLocalRandom.current());

        for (long i = 0; i <= 5; i++){
            user.setId(i);
            user.setLogin("login" + ThreadLocalRandom.current().nextInt(1, 100));
            user.setFirstname(gen.nextString());
            user.setLastname(gen.nextString());
            users.put(i, user);
        }

        user = new User(0, "kmk080405@gmail.com", "Матвей", "Карпеков");
        users.put(0L, user);
    }

    @Override
    public ArrayList<User> GetUsers() {
        ArrayList<User> result = new ArrayList<>();
        users.forEach((id, user) -> result.add(user));
        return result;
    }

    @Override
    public ArrayList<Post> GetPosts() {
        ArrayList<Post> result = new ArrayList<>();
        posts.forEach((id, post) -> result.add(post));
        return result;
    }

    @Override
    public ArrayList<Tag> GetTags() {
        ArrayList<Tag> result = new ArrayList<>();
        tags.forEach((id, tag) -> result.add(tag));
        return result;
    }

    @Override
    public ArrayList<Issue> GetIssues() {
        ArrayList<Issue> result = new ArrayList<>();
        issues.forEach((id, issue) -> result.add(issue));
        return result;
    }

    @Override
    public User GetUser(long  Id) {
        return users.get(Id);
    }

    @Override
    public Post GetPost(long  Id) {
        return posts.get(Id);
    }

    @Override
    public Tag GetTag(long  Id) {
        return tags.get(Id);
    }

    @Override
    public Issue GetIssue(long  Id) {
        return issues.get(Id);
    }

    @Override
    public User CreateUser(User user) {
        try {
            Optional<Long> BiggestId = users.keySet().stream().max(Comparator.<Long>naturalOrder());
            if (BiggestId.isPresent()) {
                user.setId(BiggestId.get() + 1);
            } else {
                user.setId(0);
            }
            users.put(user.getId(), user);
            return user;
        }catch (Exception exception){
            //user.setId(-1);
            return user;
        }
    }

    @Override
    public Post CreatePost(Post post) {
        try {
            Optional<Long> BiggestId = posts.keySet().stream().max(Comparator.<Long>naturalOrder());
            if (BiggestId.isPresent()) {
                post.setId(BiggestId.get() + 1);
            } else {
                post.setId(0);
            }
            posts.put(post.getId(), post);
            return post;
        }catch (Exception exception){
            post.setId(-1);
            return post;
        }
    }

    @Override
    public Tag CreateTag(Tag tag) {
        try {
            Optional<Long> BiggestId = tags.keySet().stream().max(Comparator.<Long>naturalOrder());
            if (BiggestId.isPresent()) {
                tag.setId(BiggestId.get() + 1);
            } else {
                tag.setId(0);
            }
            tags.put(tag.getId(), tag);
            return tag;
        }catch (Exception exception){
            tag.setId(-1);
            return tag;
        }
    }

    @Override
    public Issue CreateIssue(Issue issue) {
        try {
            Optional<Long> BiggestId = issues.keySet().stream().max(Comparator.<Long>naturalOrder());
            if (BiggestId.isPresent()) {
                issue.setId(BiggestId.get() + 1);
            } else {
                issue.setId(0);
            }
            issues.put(issue.getId(), issue);
            return issue;
        }catch (Exception exception){
            issue.setId(-1);
            return issue;
        }
    }

    @Override
    public User DeleteUser(long Id) {
        return users.remove(Id);
    }

    @Override
    public Post DeletePost(long Id) {
        return posts.remove(Id);
    }

    @Override
    public Tag DeleteTag(long Id) {
        return tags.remove(Id);
    }

    @Override
    public Issue DeleteIssue(long Id) {
        return issues.remove(Id);
    }

    @Override
    public User UpdateUser(User user) {
        if (users.containsKey(user.getId())) {
            return users.put(user.getId(), user);
        }else
            return null;
    }

    @Override
    public Post UpdatePost(Post post) {
        if (posts.containsKey(post.getId())) {
            return posts.put(post.getId(), post);
        }else
            return null;
    }

    @Override
    public Tag UpdateTag(Tag tag) {
        if (tags.containsKey(tag.getId())) {
            return tags.put(tag.getId(), tag);
        }else
            return null;
    }

    @Override
    public Issue UpdateIssue(Issue issue) {
        if (issues.containsKey(issue.getId())) {
            return issues.put(issue.getId(), issue);
        }else
            return null;
    }
}
