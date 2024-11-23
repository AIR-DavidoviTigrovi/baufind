namespace DataAccessLayer.Models;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

public record UserProfileModel 
{
    public string Name {  get; set; }
    public string Email { get; set; }
    public string Phone { get; set; }
    public string Address { get; set; }
    public byte[]? ProfilePicture { get; set; }
    public DateTime Joined { get; set; }

    public List<SkillModel> Skills { get; set; }

    public double WorkerRating { get; set; }
    public double EmployerRating { get; set; }

}