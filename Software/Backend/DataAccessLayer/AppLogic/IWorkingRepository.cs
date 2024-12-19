using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.AppLogic
{
    public interface IWorkingRepository
    {
        public bool CallWorkerToFirstAvailableEntry(int workerId, int jobId, int skillId);
        public List<WorkingModel> GetWorkingEntriesByJobId(int jobId);
    }
}
