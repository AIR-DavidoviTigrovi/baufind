namespace DataAccessLayer.Models;

public record JobWorkingModel
{
    public int JobId { get; set; }
    public int WorkingId { get; set; }
    public int? WorkerId { get; set; }
    public int StatusId { get; set; }
    public string Status { get; set; }
    public string Title { get; set; }
    public string Location { get; set; }
}
