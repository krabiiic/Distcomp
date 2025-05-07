using System.Reflection.Emit;

namespace Lab1.Core.Models
{
    public class Creator
    {
        public string Login { get; } = string.Empty;
        public string Password { get; } = string.Empty;
        public string FirstName { get; } = string.Empty;
        public string LastName { get; } = string.Empty;
        private Creator(string login, string password, string firstName, string lastName) => (Login, Password, FirstName, LastName) = (login, password, firstName, lastName);
        public static Creator Construct(string login, string password, string firstName, string lastName)
        {
            return new Creator(login, password, firstName, lastName);
        }
    }
}
