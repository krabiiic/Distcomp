package lab.datalayer;

import lab.exceptions.ForbiddenObjectException;
import lab.models.*;
import lab.models.User;
import lab.repository.IssueDao;
import lab.repository.PostDao;
import lab.repository.TagDao;
import lab.repository.UserDao;

public class FieldChecker {

    public static boolean CheckUserFields(User user, UserDao userDao) {
        if (userDao.existsByLogin(user.getLogin())) {
            throw new ForbiddenObjectException("Duplicate login");
        }

        return stringLengthInRange(user.getLogin(), 2, 64)
                && stringLengthInRange(user.getFirstname(), 2, 64)
                && stringLengthInRange(user.getLastname(), 2, 64)
                && stringLengthInRange(user.getPassword(), 8, 128);
    }

    public static boolean CheckIssueFields(Issue issue, IssueDao issueDao) {
        if (issueDao.existsByTitle(issue.getTitle())) {
            throw new ForbiddenObjectException("Duplicate title");
        }
        return stringLengthInRange(issue.getContent(), 4, 2048)
                && stringLengthInRange(issue.getTitle(), 2, 64);
    }

    public static boolean CheckTagFields(Tag tag, TagDao tagDao) {
        return stringLengthInRange(tag.getName(), 2, 32);
    }

    public static boolean CheckPostFields(Post post, PostDao postDao) {
        return stringLengthInRange(post.getContent(), 2, 2048);
    }

    private static boolean stringLengthInRange(String str, int low, int high) {
        return str.length() >= low && str.length() <= high;
    }
}
