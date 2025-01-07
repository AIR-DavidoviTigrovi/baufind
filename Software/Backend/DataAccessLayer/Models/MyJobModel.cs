namespace DataAccessLayer.Models;

/// <summary>
/// Sadrži informacije o poslu korisnika ili posllovima na koje je dodan
/// </summary>
public record MyJobModel
{
    public int Id { get; set; }
    public int EmployerId { get; set; }
    public int JobStatusId { get; set; }
    public string JobStatus { get; set; }
    public string Title { get; set; }
    public string Description { get; set; }
    public bool AllowWorkerInvite { get; set; }
    public string Location { get; set; }
    public decimal? Lat { get; set; }
    public decimal? Lng { get; set; }
    public bool UserIsEmployer { get; set; }
}
