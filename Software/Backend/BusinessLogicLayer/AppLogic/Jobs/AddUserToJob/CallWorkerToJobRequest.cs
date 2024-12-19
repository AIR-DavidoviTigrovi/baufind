using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.AddUserToJob
{
    public record CallWorkerToJobRequest
    {
        public int WorkerId { get; set; }
        public int JobId { get; set; }
        public int SkillId { get; set; }
    }
}
