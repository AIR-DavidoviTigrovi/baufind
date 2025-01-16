using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.GetJobHistory
{
    public record GetJobHistoryResponse
    {
        public List<JobHistoryRecord> jobHistory { get; set; }
        public string? Error { get; set; }
    }
}
