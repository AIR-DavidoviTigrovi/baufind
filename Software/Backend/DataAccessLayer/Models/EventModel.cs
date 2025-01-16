using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record EventModel
    {
        public string EventName { get; set; }
        public string Date { get; set; }
    }
}
