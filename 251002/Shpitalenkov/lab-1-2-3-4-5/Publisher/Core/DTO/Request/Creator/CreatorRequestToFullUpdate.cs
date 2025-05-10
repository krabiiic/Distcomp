namespace Core.DTO;

public class CreatorRequestToFullUpdate
{
    public long Id { get; init; }
    public string Login { get; init; }
    public string Password { get; init; }
    public string Firstname { get; init; }
    public string? Lastname { get; init; }
}