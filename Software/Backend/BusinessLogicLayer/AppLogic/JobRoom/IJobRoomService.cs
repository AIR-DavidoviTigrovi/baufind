using BusinessLogicLayer.AppLogic.JobRoom.GetJobRoom;
using BusinessLogicLayer.AppLogic.JobRoom.SetRoomStatus;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
namespace BusinessLogicLayer.AppLogic.JobRoom {
    public interface IJobRoomService {
        public GetJobRoomResponse GetJobRoomResponse(int jobId);

        public SetRoomStatusResponse SetRoomStatusResponse(int jobId, int status);
    }
}
