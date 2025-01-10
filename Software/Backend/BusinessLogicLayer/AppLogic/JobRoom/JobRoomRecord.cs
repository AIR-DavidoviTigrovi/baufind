using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.JobRoom {
    public record JobRoomRecord {
        public int WorkingId { get; set; }
        public int JobId { get; set; }
        public string JobTitle { get; set; }

        public int EmployerId { get; set; }
        public bool AllowWorkerInvite { get; set; }
        public int SkillId { get; set; }
        public string SkillTitle { get; set; }
        public int? WorkerId { get; set; }
        public string WorkerName { get; set; }
        public string WorkingStatus { get; set; }
    }
}
