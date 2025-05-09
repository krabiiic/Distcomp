using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab3.Application.Contracts
{
    // PostRequestTo сериализуется в Data поля общего PostRequest
    public record PostRequestTo(ulong NewsId, string Content);
}
