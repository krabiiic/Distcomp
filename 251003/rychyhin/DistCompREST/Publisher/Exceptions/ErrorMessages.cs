namespace Publisher.Exceptions;

public static class ErrorMessages
{
    public static string EditorNotFoundMessage(long id) => $"Editor with id {id} was not found.";
    public static string NewsNotFoundMessage(long id) => $"News with id {id} was not found.";
    public static string LabelNotFoundMessage(long id) => $"Label with id {id} was not found.";
    public static string NoteNotFoundMessage(long id) => $"Note with id {id} was not found.";
}