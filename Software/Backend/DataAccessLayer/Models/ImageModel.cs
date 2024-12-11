using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record ImageModel
    {
        public int Id { get; set; }
        public required byte[] Picture { get; set; }
    }
}
