using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record MyJobNotificationModel
    {      
        public int WorkerId { get; set; }       
        public string Name { get; set; }       
        public string Address { get; set; }      
        public int SkillId { get; set; }         
        public int JobId { get; set; }           
        public string JobTitle { get; set; }    
        public int WorkingStatusId { get; set; } 
        public decimal Rating { get; set; }
        public string Skill { get; set; }
        public int CompletedJobsCount { get; set; }
    }
}
