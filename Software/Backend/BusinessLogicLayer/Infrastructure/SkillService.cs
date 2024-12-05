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
        private readonly ISkillRepository _skillRepository;
        private readonly IJwtService _jwtService;

        public SkillService(ISkillRepository skillRepository, IJwtService jwtService)
        {
            _skillRepository = skillRepository;
            _jwtService = jwtService;
        }
        /// <summary>
        /// Vraća sve skillove ili error message ako je tablica prazna
        /// </summary>
        /// <returns></returns>
        public GetAllSkillsResponse GetAllSkills()
        {
            var query = _skillRepository.GetAll();
            if (query == null || !query.Any())
            {
                return new GetAllSkillsResponse()
                {
                    Error = "U bazi nema pozicija!"
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
