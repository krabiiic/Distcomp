using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Redis.OM.Modeling;

namespace DistComp.Models {
    public abstract class EntityWithId {

        [Key, Column("id")]
        public long Id { get; set; } = 0;
    }
}
