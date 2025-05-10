namespace DistComp_1.Exceptions;

public static class ErrorMessages
{
    public static string UserNotFoundMessage(long id) => $"User with id {id} was not found.";
    public static string ArticleNotFoundMessage(long id) => $"Article with id {id} was not found.";
    public static string MarkNotFoundMessage(long id) => $"Mark with id {id} was not found.";
    public static string MessageNotFoundMessage(long id) => $"Message with id {id} was not found.";

    public static string UserAlreadyExists(string login) => $"User with login '{login}' already exists.";
    public static string ArticleAlreadyExists(string title) => $"Article with title '{title}' already exists.";
    public static string MarkAlreadyExists(string Mark) => $"Mark with name '{Mark}' already exists.";
}