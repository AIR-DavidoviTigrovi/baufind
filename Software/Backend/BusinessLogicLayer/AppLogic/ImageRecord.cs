using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic
{
    public record ImageRecord
    {
        public int Id { get; set; }
        public required byte[] Picture { get; set; }
    }
}
