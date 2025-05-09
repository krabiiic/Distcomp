
namespace Lab3.Application.Exceptions
{
    public class IncorrectDatabaseException : Exception
    {
        public IncorrectDatabaseException(string? msg, Exception? inner = null) : base(msg, inner)
        {

        }
    }
}
