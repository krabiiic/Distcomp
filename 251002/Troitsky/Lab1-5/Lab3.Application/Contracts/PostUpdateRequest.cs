using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab3.Application.Contracts
{
    // PostUpdateRequest сериализуется в Data поля общего PostRequest
    public record PostUpdateRequest(ulong Id, ulong NewsId, string Content);
}
