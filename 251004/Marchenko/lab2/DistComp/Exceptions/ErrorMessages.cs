namespace DistComp.Exceptions;

public static class ErrorMessages
{
    public static string CreatorNotFoundMessage(long id) => $"Creator with id {id} was not found.";
    public static string IssueNotFoundMessage(long id) => $"Issue with id {id} was not found.";
    public static string MarkNotFoundMessage(long id) => $"Mark with id {id} was not found.";
    public static string MessageNotFoundMessage(long id) => $"Message with id {id} was not found.";
}