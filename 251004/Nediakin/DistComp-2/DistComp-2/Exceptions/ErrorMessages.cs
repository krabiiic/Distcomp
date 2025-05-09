namespace DistComp.Exceptions;

public static class ErrorMessages
{
    public static string CreatorNotFoundMessage(long id) => $"Creator with id {id} was not found.";
    public static string IssueNotFoundMessage(long id) => $"Issue with id {id} was not found.";
    public static string StickerNotFoundMessage(long id) => $"Sticker with id {id} was not found.";
    public static string NoteNotFoundMessage(long id) => $"Note with id {id} was not found.";
    public static string CreatorAlreadyExists(string login) => $"User with login '{login}' already exists.";
    public static string IssueAlreadyExists(string title) => $"Story with title '{title}' already exists.";
    public static string StickerAlreadyExists(string tag) => $"Tag with name '{tag}' already exists.";
}