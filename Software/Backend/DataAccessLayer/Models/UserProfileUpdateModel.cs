using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    /// <summary>
    /// Model koji predstavlja kako izgleda azuriranje korisnika(tj njegovog profila)
    /// </summary>
    public record UserProfileUpdateModel
    {
        public int UserId { get; set; }
        public string? Name { get; set; }
        public string? Address { get; set; }
        public string? Phone { get; set; }
        public byte[]? ProfilePicture { get; set; }
        public List<int>? AddSkills { get; set; } = new List<int>();
        public List<int>? RemoveSkills { get; set; } = new List<int>();

    }
}
