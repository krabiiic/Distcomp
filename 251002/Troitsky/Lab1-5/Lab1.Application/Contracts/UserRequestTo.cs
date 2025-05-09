namespace Lab1.Application.Contracts
{
    // DTO, определяющее формат для создания сущности User
    // Для создания нового объекта User, id генерируется на сервере

    public record UserRequestTo(string Login, string Password, string FirstName, string LastName);
}
