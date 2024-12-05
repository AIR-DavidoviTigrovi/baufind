namespace DataAccessLayer.Models
{
    public record JobModel
    {
        public int Id { get; set; }
        public int Employer_id { get; set; }
        public int Job_status_id { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public bool Allow_worker_invite { get; set; }
        public string Location { get; set; }
        public decimal? Lat { get; set; }
        public decimal? Lng { get; set; }
    }
}
