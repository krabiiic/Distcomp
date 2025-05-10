package publisher.datalayer;

import publisher.exceptions.ForbiddenObjectException;
import publisher.models.Issue;
import publisher.models.Tag;
import publisher.models.User;
import publisher.repository.IssueDao;
import publisher.repository.TagDao;
import publisher.repository.UserDao;

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

    private static boolean stringLengthInRange(String str, int low, int high) {
        return str.length() >= low && str.length() <= high;
    }
}
