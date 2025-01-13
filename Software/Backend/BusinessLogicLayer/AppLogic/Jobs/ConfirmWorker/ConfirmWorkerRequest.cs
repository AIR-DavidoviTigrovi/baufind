using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.ConfirmWorker
{
    public record ConfirmWorkerRequest
    {
        public int WorkerId { get; set; }
        public int JobId { get; set; }
        public int SkillId { get; set; }

        public int WorkingStatusId { get; set; }
    }
}
