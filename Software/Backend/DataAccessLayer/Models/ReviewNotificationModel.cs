using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record ReviewNotificationModel
    {
        public int JobId { get; set; }
        public string JobTitle { get; set; } = string.Empty;
        public int PersonId { get; set; }     
        public string PersonName { get; set; } = string.Empty;
        public string Position { get; set; }
        public int? WorkingId { get; set; } 

    }
}
