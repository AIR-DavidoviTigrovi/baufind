namespace BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser
{
    public record GetJobsForCurrentUserResponse
    {
        public List<JobRecord> Jobs { get; set; } = new List<JobRecord>();
        public string? Error { get; set; } = string.Empty;
    }
}
