using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.JobRoom.SetRoomStatus {
    public class SetRoomStatusRequest {
        public int jobID { get; set; }
        public int status { get; set; }
    }
}
