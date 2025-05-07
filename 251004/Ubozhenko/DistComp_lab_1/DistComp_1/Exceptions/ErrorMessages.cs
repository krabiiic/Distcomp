namespace DistComp_1.Exceptions;

public static class ErrorMessages
{
    public static string AuthorNotFoundMessage(long id) => $"Author with id {id} was not found.";
    public static string NewsNotFoundMessage(long id) => $"News with id {id} was not found.";
    public static string LabelNotFoundMessage(long id) => $"Label with id {id} was not found.";
    public static string ReactionNotFoundMessage(long id) => $"Reaction with id {id} was not found.";

    public static string AuthorAlreadyExists(string login) => $"Author with login '{login}' already exists.";
    public static string NewsAlreadyExists(string title) => $"News with title '{title}' already exists.";
    public static string LabelAlreadyExists(string tag) => $"Label with name '{tag}' already exists.";
}