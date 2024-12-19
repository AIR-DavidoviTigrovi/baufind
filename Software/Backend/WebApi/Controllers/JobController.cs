using Azure;
using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Jobs.AddUserToJob;
using BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;
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
                Error = "Ne mo�ete pristupiti tom resursu!"
            });
        }

        var response = _jobService.AddJob(request, userIdFromJwt.Value);

        if (!string.IsNullOrEmpty(response.Error))
        {

            return BadRequest(response);
        }

        return response;
    }

    // GET: /jobsForCurrentUser
    [HttpGet("jobsForCurrentUser")]
    [Authorize]
    public ActionResult<GetJobsForCurrentUserResponse> GetJobsForCurrentUser()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobsForCurrentUserResponse()
            {
                Error = "Ne mo�ete pristupiti tom resursu!"
            });
        }

        var jobs = _jobService.GetJobsForCurrentUser(userIdFromJwt.Value);
        
        return jobs;
    }

    [HttpPut("CallForWorking")]
    [Authorize]
    public ActionResult<CallWarkerToJobResponse> CallWorkerToJob([FromBody] CallWorkerToJobRequest request)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new CallWarkerToJobResponse()
            {
                Success = false,
                Message = "Ne mo�ete pristupiti tom resursu!"
            });
        }
        return _jobService.CallWorkerToJob(request);

    }

}