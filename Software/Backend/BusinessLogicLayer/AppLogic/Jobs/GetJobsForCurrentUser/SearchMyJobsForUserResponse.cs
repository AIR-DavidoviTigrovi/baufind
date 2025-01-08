using DataAccessLayer.Models;

namespace BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;

public class SearchMyJobsForUserResponse
{
    public bool Success { get; set; } = false;
    public List<MyJobModel> Jobs { get; set; } = new List<MyJobModel>();
    public string? Error { get; set; } = string.Empty;
}
