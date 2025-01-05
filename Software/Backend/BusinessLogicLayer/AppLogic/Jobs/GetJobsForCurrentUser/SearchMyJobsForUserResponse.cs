using DataAccessLayer.Models;

namespace BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;

public record SearchMyJobsForUserResponse
{
    public List<JobWorkingModel> Jobs { get; set; } = new List<JobWorkingModel>();
    public string? Error { get; set; } = string.Empty;
}
