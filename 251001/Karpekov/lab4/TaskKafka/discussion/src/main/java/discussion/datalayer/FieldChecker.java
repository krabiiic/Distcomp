package discussion.datalayer;

import discussion.models.Post;
import discussion.repository.PostDao;

public class FieldChecker {

    public static boolean CheckPostFields(Post post, PostDao postDao) {
        return stringLengthInRange(post.getContent(), 2, 2048);
    }

    private static boolean stringLengthInRange(String str, int low, int high) {
        return str.length() >= low && str.length() <= high;
    }
}
