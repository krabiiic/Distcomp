namespace Core.DTO;

public class CreatorRequestToCreate
{
    public string Login { get; init; }
    public string Password { get; init; }
    public string Firstname { get; init; }
    public string? Lastname { get; init; }
}