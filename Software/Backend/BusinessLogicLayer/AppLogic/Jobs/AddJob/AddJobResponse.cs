namespace BusinessLogicLayer.AppLogic.Jobs.AddJob
{
    public record AddJobResponse
    {
        public string? Success { get; set; } = string.Empty;
        public string? Error { get; set; } = string.Empty;
    }
}
