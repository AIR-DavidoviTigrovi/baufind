using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.WorkerJoinJob
{
    public record WorkerRequestJoinRequest
    {
        public int JobId { get; set; }
        public int SkillId { get; set; }
    }
}
