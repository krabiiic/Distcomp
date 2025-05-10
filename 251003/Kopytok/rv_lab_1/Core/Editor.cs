using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core
{
    public class Editor
    {
        [Column("id")]
        public long Id { get; set; } = 0;
        [Column("login")]
        public string Login { get; set; } = string.Empty;
        [Column("password")]
        public string Password { get; set; } = string.Empty;
        [Column("firstname")]
        public string FirstName { get; set; } = string.Empty;
        [Column("lastname")]
        public string LastName { get; set; } = string.Empty;

        public ICollection<Story> Stories { get; set; }
    }
}
