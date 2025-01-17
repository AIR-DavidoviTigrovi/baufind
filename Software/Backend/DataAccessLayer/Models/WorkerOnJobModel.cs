using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record WorkerOnJobModel
    {
        public string WorkerName { get; set; }
        public string RoleTitle { get; set; }
    }
}
