using System.Reflection.Emit;

namespace Lab1.Core.Models
{
    public class User
    {
        public string Login { get; } = string.Empty;
        public string Password { get; } = string.Empty;
        public string FirstName { get; } = string.Empty;
        public string LastName { get; } = string.Empty;
        private User(string login, string password, string firstName, string lastName) => (Login, Password, FirstName, LastName) = (login, password, firstName, lastName);
        // Позволяет контролировать создание, например, для валидации данных в одном месте
        public static User Construct(string login, string password, string firstName, string lastName)
        {
            return new User(login, password, firstName, lastName);
        }
    }
}
