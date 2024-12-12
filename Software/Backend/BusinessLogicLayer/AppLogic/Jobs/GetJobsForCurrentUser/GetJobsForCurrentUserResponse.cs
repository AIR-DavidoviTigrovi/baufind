using DataAccessLayer.Models;

namespace BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser
{
    public record GetJobsForCurrentUserResponse
    {
        public List<JobSearchModel> Jobs { get; set; } = new List<JobSearchModel>();
        public string? Error { get; set; } = string.Empty;
    }
}
