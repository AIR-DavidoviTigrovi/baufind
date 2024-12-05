namespace BusinessLogicLayer.AppLogic.Jobs
{
    public record JobRecord
    {
        public int Id { get; set; }
        public int Employer_id { get; set; }
        public int Job_status_id { get; set; }
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
