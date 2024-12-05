using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("jobs")]
public class JobController : ControllerBase
{
    private readonly IJobService _jobService;
    public JobController(IJobService jobService)
    {
        _jobService = jobService;
    }

    // POST: /jobs/add
    [HttpPost("add")]
    [Authorize]
    public ActionResult<AddJobResponse> AddJob([FromBody] AddJobRequest request)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new AddJobResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }

        var job = _jobService.AddJob(request, userIdFromJwt.Value);

        if (job.Error != null)
        {
            return BadRequest(job);
        }

        return job;
    }
}