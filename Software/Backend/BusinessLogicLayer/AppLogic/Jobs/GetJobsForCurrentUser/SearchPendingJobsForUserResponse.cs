using DataAccessLayer.Models;

namespace BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;

public record SearchPendingJobsForUserResponse
{
    public bool Success { get; set; } = false;
    public List<JobWorkingModel> Jobs { get; set; } = new List<JobWorkingModel>();
    public string? Error { get; set; } = string.Empty;
}
