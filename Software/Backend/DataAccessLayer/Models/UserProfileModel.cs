namespace DataAccessLayer.Models;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

/// <summary>
/// UserProfileModel koji koristi sve podatke potrebne za prikaz profila.
/// </summary>
public record UserProfileModel
{
    public required string Name { get; set; }
    public required string Email { get; set; }
    public required string Phone { get; set; }
    public required string Address { get; set; }
    public byte[]? ProfilePicture { get; set; }
    public DateTime Joined { get; set; }
    public List<SkillModel> Skills { get; set; } = new List<SkillModel>();


    public double WorkerRating { get; set; }
    public double EmployerRating { get; set; }
    public ReviewModel? Reviews { get; set; }
}

public class ReviewModel
{
    public double AverageRating { get; set; }
    public int TotalReviews { get; set; }
    public List<int> Ratings { get; set; } = new List<int>(); 
}