using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Jobs.AddUserToJob;
using BusinessLogicLayer.AppLogic.Jobs.ConfirmWorker;
using BusinessLogicLayer.AppLogic.Jobs.GetAllJobsHistory;
using BusinessLogicLayer.AppLogic.Jobs.GetJob;
using BusinessLogicLayer.AppLogic.Jobs.GetJobHistory;
using BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;
using BusinessLogicLayer.AppLogic.Jobs.WorkerJoinJob;

namespace BusinessLogicLayer.AppLogic.Jobs
{
    public interface IJobService
    {
        public AddJobResponse AddJob(AddJobRequest request, int userId);
        public GetJobsForCurrentUserResponse GetJobsForCurrentUser(int userId);
        public GetJobResponse GetJob(int jobId, int userId);
        public CallWarkerToJobResponse CallWorkerToJob(CallWorkerToJobRequest request, int userId);
        public PendingInvitationResponse GetPendingInvitations(int userId);
        public SearchPendingJobsForUserResponse SearchPendingJobsForUser(int userId);
        public SearchMyJobsForUserResponse SearchMyJobsForUser(int userId);
        public WorkerRequestJoinResponse WorkerRequestJoin(WorkerRequestJoinRequest request, int userId);
        public ConfirmWorkerResponse ConfirmWorkerRequest(ConfirmWorkerRequest request);
        public MyJobsNotificationResponse GetMyJobsNotifications(int EmployerId);
        public GetAllJobsHistoryResponse GetAllJobsHistory(int userId);
        public GetJobHistoryResponse GetJobHistory(int jobId, int userId);
    }
}
