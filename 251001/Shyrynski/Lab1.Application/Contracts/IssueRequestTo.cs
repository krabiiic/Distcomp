using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab1.Application.Contracts
{
    public record IssueRequestTo(ulong CreatorId, string Title, string Content);
}
