using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser
{
    public record PendingInvitationResponse
    {
        public List<JobNotificationModel> Jobs { get; set; } = new List<JobNotificationModel>();
        public string? Error { get; set; } = string.Empty;
    }
}
