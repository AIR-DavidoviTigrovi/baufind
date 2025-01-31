using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.GetAllJobsHistory
{
    public record GetAllJobsHistoryResponse
    {
        public List<AllJobsHistoryRecord> Jobs { get; set; }
        public string Error { get; set; }
    }
}
