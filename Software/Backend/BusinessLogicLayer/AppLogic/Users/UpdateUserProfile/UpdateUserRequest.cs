using BusinessLogicLayer.AppLogic.Skills;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Users.UpdateUserProfile
{
    public record UpdateUserRequest
    {
        public int UserId { get; set; }
        public string? Name { get; set; }
        public string? Address { get; set; }
        public string? Phone { get; set; }
        public string? ProfilePicture { get; set; }
        public List<int>? AddSkills { get; set; } = new List<int>();
        public List<int>? RemoveSkills { get; set; } = new List<int>();
    }
}
