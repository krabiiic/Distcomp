namespace DistComp_1.Exceptions;

public static class ErrorMessages
{
    public static string CreatorNotFoundMessage(long id) => $"Creator with id {id} was not found.";
    public static string IssueNotFoundMessage(long id) => $"Issue with id {id} was not found.";
    public static string StickerNotFoundMessage(long id) => $"Sticker with id {id} was not found.";
    public static string NoteNotFoundMessage(long id) => $"Note with id {id} was not found.";

    public static string CreatorAlreadyExists(string login) => $"Creator with login '{login}' already exists.";
    public static string IssueAlreadyExists(string title) => $"Issue with title '{title}' already exists.";
    public static string StickerAlreadyExists(string tag) => $"Sticker with name '{tag}' already exists.";
}