namespace DistComp_1.Exceptions;

public static class ErrorMessages
{
    public static string CreatorNotFoundMessage(long id) => $"Creator with id {id} was not found.";
    public static string IssueNotFoundMessage(long id) => $"Issue with id {id} was not found.";
    public static string MarkNotFoundMessage(long id) => $"Mark with id {id} was not found.";
    public static string MessageNotFoundMessage(long id) => $"Message with id {id} was not found.";

    public static string CreatorAlreadyExists(string login) => $"Creator with login '{login}' already exists.";
    public static string IssueAlreadyExists(string title) => $"Issue with title '{title}' already exists.";
    public static string MarkAlreadyExists(string mark) => $"Mark with name '{mark}' already exists.";
}