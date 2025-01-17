using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic
{
    public record ImageStringRecord
    {
        public int Id { get; set; }
        public required string Picture { get; set; }
    }
}
