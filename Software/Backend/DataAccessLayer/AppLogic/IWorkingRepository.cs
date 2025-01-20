using Azure.Core;
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
        public (bool, string) AddNewWorkingEntry(int workerId, int jobId, int skillId, int userId);

        public List<JobNotificationModel> GetPendingInvitations(int workerId);
        public bool InsertWorkerRequestToWorking(int userId, int jobId, int skillId);

        public (bool, string) ConfirmWorker(int JobId, int WorkerId, int SkillId, int WorkingStatusId);

        public (bool, string) WorkerConfirmJob(int JobId, int WorkerId, int WorkingStatusId);

        public List<MyJobNotificationModel> GetPendingJobApplications(int jobId);
    }
}
