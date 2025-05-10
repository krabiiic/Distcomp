namespace Publisher.Exceptions;

public static class ErrorMessages
{
    public static string EditorNotFoundMessage(long id) => $"Editor with id {id} was not found.";
    public static string ArticleNotFoundMessage(long id) => $"Article with id {id} was not found.";
    public static string MarkNotFoundMessage(long id) => $"Mark with id {id} was not found.";
    public static string PostNotFoundMessage(long id) => $"Post with id {id} was not found.";
}