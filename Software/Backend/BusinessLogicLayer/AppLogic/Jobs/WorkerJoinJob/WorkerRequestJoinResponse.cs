using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.WorkerJoinJob
{
    public record WorkerRequestJoinResponse
    {
        public bool Success { get; set; }
        public string? Message { get; set; }
    }
}
