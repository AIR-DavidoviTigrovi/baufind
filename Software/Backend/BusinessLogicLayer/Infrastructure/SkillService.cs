using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Skills;
using BusinessLogicLayer.AppLogic.Skills.GetAllSkills;
using DataAccessLayer.AppLogic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.Infrastructure
{
    public class SkillService : ISkillService
    {
        private readonly ISkillRepository _repository;
        private readonly IJwtService _jwtService;

        public SkillService(ISkillRepository repository, IJwtService jwtService)
        {
            _repository = repository;
            _jwtService = jwtService;
        }
        /// <summary>
        /// Metoda koja dohvaca sve skillove povezane s korisnikom
        /// </summary>
        /// <returns>GetAllSkillsResponse()</returns>
        public GetAllSkillsResponse GetAllSkills()
        {
            var query = _repository.GetAllSkills();
            if(query == null || !query.Any())
            {
                return new GetAllSkillsResponse()
                {
                    Error = "U bazi nema skill-ova"
                };
            }
            var skills = query.Select(x => new SkillRecord()
            {
                Id = x.Id,
                Title = x.Title
            }).ToList();

            return new GetAllSkillsResponse()
            {
                Skills = skills
            };
        }
    }
}
