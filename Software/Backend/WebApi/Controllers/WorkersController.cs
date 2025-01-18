using BusinessLogicLayer.AppLogic.Users.GetUser;
using BusinessLogicLayer.AppLogic.Workers;
using BusinessLogicLayer.AppLogic.Workers.GetWorkers;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers {
    [Route("workers")]
    [ApiController]
    public class WorkersController : ControllerBase {

        private readonly IWorkersService _workersService;
        public WorkersController(IWorkersService workersService)
        {
            this._workersService = workersService;
        }
        // GET: /workers/{skill}
        [HttpGet("{skill}/{workerIDs}")]
        [Authorize]
        public ActionResult<GetWorkersResponse> GetWorkers(string skill,string workerIDs)
        {
            var userIdFromJwt = HttpContext.Items["UserId"] as int?;

            if (userIdFromJwt == null) {
                return Unauthorized(new GetUserResponse() {
                    Error = "Ne možete pristupiti tom resursu!"
                });
            }
            List<int> workerList = workerIDs
          .Trim('[', ']')  
          .Split(',')     
          .Select(int.Parse)
          .ToList();
            
            workerList.Add(userIdFromJwt.Value);
            
            string updatedWorkerIDs = "[" + string.Join(",", workerList) + "]";
            var workers = _workersService.GetWorkers(skill, updatedWorkerIDs);
            if (workers.WorkerRecords == null || !workers.WorkerRecords.Any())
            {
                return NotFound(workers);
            }
            return workers;
        }
    }
}
