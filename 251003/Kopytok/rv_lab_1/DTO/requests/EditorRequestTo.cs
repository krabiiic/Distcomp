using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTO.requests
{
    public class EditorRequestTo
    {
        [StringLength(64, MinimumLength = 2)]
        public string login { get; set; } = string.Empty;

        [StringLength(128, MinimumLength = 8)]
        public string password { get; set; } = string.Empty;

        [StringLength(64, MinimumLength = 2)]
        public string firstname { get; set; } = string.Empty;

        [StringLength(64, MinimumLength = 2)]
        public string lastname { get; set; } = string.Empty;
    }
}
