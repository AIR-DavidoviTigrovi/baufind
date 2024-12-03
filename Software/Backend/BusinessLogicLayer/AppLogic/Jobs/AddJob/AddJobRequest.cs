namespace BusinessLogicLayer.AppLogic.Jobs.AddJob
{
    public record AddJobRequest
    {
        public string Title { get; set; }
        public string Description { get; set; }
        public bool Allow_worker_invite { get; set; }
        public string Location { get; set; }
        public decimal? Latitude { get; set; }
        public decimal? Longitude { get; set; }
        public List<byte[]> Pictures { get; set; }
        public List<int> Skills { get; set; }
    }
}
