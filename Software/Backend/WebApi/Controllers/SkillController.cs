
using BusinessLogicLayer.AppLogic.Skills;
using BusinessLogicLayer.AppLogic.Skills.GetAllSkills;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("skills")]

public class SkillController : ControllerBase
{
    private readonly ISkillService _skillService;

    public SkillController(ISkillService skillService)
    {
        _skillService = skillService;
    }

    // GET: /skills
    [HttpGet]
    [Authorize]
    public ActionResult<GetAllSkillsResponse> GetAll()
    {
        var skills = _skillService.GetAllSkills();

        if (skills.Skills == null || skills.Skills.Count == 0)
        {
            return NotFound(skills);
        }

        return skills;
    }
}
