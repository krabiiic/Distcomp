namespace Publisher.Exceptions;

public static class ErrorMessages
{
    public static string UserNotFoundMessage(long id) => $"User with id {id} was not found.";
    public static string NewsNotFoundMessage(long id) => $"News with id {id} was not found.";
    public static string LabelNotFoundMessage(long id) => $"Label with id {id} was not found.";
    public static string NoticeNotFoundMessage(long id) => $"Notice with id {id} was not found.";
}