using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Skills.GetAllSkills
{
    public record GetAllSkillsResponse
    {
        public List<SkillRecord> Skills { get; set; } = new List<SkillRecord>();
        public string? Error { get; set; } = string.Empty;
    }
}
