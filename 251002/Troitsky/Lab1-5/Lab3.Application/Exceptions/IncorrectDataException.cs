
namespace Lab3.Application.Exceptions
{
    public class IncorrectDataException : Exception
    {
        public IncorrectDataException(string? msg, Exception? inner = null) : base(msg, inner)
        {

        }
    }
}
