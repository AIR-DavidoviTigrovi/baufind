using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.JobRoom.GetJobRoom {
    public record GetJobRoomResponse {
        public List<JobRoomRecord> JobRooms { get; set; } = new List<JobRoomRecord>();
        public string? Error { get; set; } = string.Empty;
    }
}
