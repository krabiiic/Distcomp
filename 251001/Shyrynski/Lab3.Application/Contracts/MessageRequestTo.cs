using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab3.Application.Contracts
{
    public record MessageRequestTo(ulong IssueId, string content);
}
