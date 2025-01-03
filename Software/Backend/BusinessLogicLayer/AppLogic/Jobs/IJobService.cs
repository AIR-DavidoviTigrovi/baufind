using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Jobs.AddUserToJob;
using BusinessLogicLayer.AppLogic.Jobs.GetJob;
using BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs
{
    public interface IJobService
    {
        public AddJobResponse AddJob(AddJobRequest request, int userId);
        public GetJobsForCurrentUserResponse GetJobsForCurrentUser(int userId);
        public GetJobResponse GetJob(int jobId);
        public CallWarkerToJobResponse CallWorkerToJob(CallWorkerToJobRequest request, int userId);
        public PendingInvitationResponse GetPendingInvitations(int userId);
    }
}
     