using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace Lab1.Application.Exceptions
{
    public class IncorrectDataException : Exception
    {
        public IncorrectDataException(string? msg, Exception? inner = null) : base(msg, inner)
        {

        }
    }
}
