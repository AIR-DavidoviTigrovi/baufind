using BusinessLogicLayer.AppLogic.Skills.GetAllSkills;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Skills
{
    public interface ISkillService
    {
        public GetAllSkillsResponse GetAllSkills();
    }
}
