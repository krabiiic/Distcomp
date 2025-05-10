namespace Publisher.Exceptions;

public static class ErrorMessages
{
    public static string AuthorNotFoundMessage(long id) => $"Author with id {id} was not found.";
    public static string NewsNotFoundMessage(long id) => $"News with id {id} was not found.";
    public static string LabelNotFoundMessage(long id) => $"Label with id {id} was not found.";
    public static string ReactionNotFoundMessage(long id) => $"Reaction with id {id} was not found.";
}