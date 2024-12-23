using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.AppLogic {
    public interface IJobRoomRepository {

        public List<JobRoomModel>? GetJobRoom(int jobID);

    }
}
