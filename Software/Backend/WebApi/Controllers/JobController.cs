using Azure;
using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Jobs.AddUserToJob;
using BusinessLogicLayer.AppLogic.Jobs.ConfirmWorker;
using BusinessLogicLayer.AppLogic.Jobs.GetAllJobsHistory;
using BusinessLogicLayer.AppLogic.Jobs.GetJob;
using BusinessLogicLayer.AppLogic.Jobs.GetJobHistory;
using BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;
using BusinessLogicLayer.AppLogic.Jobs.WorkerJoinJob;
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

        var response = _jobService.AddJob(request, userIdFromJwt.Value);

        if (!string.IsNullOrEmpty(response.Error))
        {

            return BadRequest(response);
        }

        return response;
    }

    // GET: /jobs/search
    [HttpGet("search")]
    [Authorize]
    public ActionResult<GetJobsForCurrentUserResponse> GetJobsForCurrentUser()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobsForCurrentUserResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }

        var jobs = _jobService.GetJobsForCurrentUser(userIdFromJwt.Value);

        return jobs;
    }

    //GET: /jobs/{id}
    [HttpGet("{id}")]
    [Authorize]
    public ActionResult<GetJobResponse> GetJob(int id)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }

        int userId = userIdFromJwt.Value;

        var job = _jobService.GetJob(id, userId);

        return job;

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
                Message = "Ne možete pristupiti tom resursu!"
            });
        }
        return _jobService.CallWorkerToJob(request, userIdFromJwt.Value);

    }

    [HttpGet("CheckJobNotifications")]
    [Authorize]
    public ActionResult<PendingInvitationResponse> GetPendindInvitations()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobsForCurrentUserResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }

        var jobs = _jobService.GetPendingInvitations(userIdFromJwt.Value);

        return jobs;
    }

    [HttpGet("SearchPendingJobsForUser")]
    [Authorize]
    public ActionResult<SearchPendingJobsForUserResponse> SearchPendingJobsForUser()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }

        var jobs = _jobService.SearchPendingJobsForUser(userIdFromJwt.Value);

        return jobs;
    }

    [HttpGet("SearchMyJobsForUser")]
    [Authorize]
    public ActionResult<SearchMyJobsForUserResponse> SearchMyJobsForUser()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }

        var jobs = _jobService.SearchMyJobsForUser(userIdFromJwt.Value);

        return jobs;
    }

    [HttpPost("requestJoin")]
    [Authorize]
    public ActionResult<WorkerRequestJoinResponse> WorkerRequestJoin([FromBody] WorkerRequestJoinRequest request)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }

        var userId = userIdFromJwt;

        var response = _jobService.WorkerRequestJoin(request, userId.Value);

        return response;
    }

    [HttpPut("confirmWorker")]
    [Authorize]
    public ActionResult<ConfirmWorkerResponse> ConfirmWorkerRequest(ConfirmWorkerRequest request)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }
        var response = _jobService.ConfirmWorkerRequest(request);

        return response;
    }
    [HttpPut("workerConfirmsJob")]
    [Authorize]
    public ActionResult<ConfirmWorkerResponse> WorkerConfirmsJob(ConfirmWorkerRequest request) {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null) {
            return Unauthorized(new GetJobResponse() {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }
        request.WorkerId = userIdFromJwt.Value;
        var response = _jobService.WorkerConfirmsJob(request);

        return response;
    }

    [HttpGet("getMyJobNotifications")]
    [Authorize]
    public ActionResult<MyJobsNotificationResponse> GertMyJobNotifications()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }
        else
        {
            return _jobService.GetMyJobsNotifications((int)userIdFromJwt);
        }
    }

    [HttpGet("getHistory")]
    [Authorize]
    public ActionResult<GetAllJobsHistoryResponse> GetAllJobsHistory()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;
        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetAllJobsHistoryResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }
        else
        {
            return _jobService.GetAllJobsHistory(userIdFromJwt.Value);
        }
    }

    [HttpGet("history/{id}")]
    [Authorize]
    public ActionResult<GetJobHistoryResponse> GetJobHistory(int id)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;
        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetJobHistoryResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }
        else
        {
            return _jobService.GetJobHistory(id, userIdFromJwt.Value);
        }
    }
}