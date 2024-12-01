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
        [HttpGet("{skill}")]
        [Authorize]
        public ActionResult<GetWorkersResponse> GetWorkers(string skill)
        {
            var workers = _workersService.GetWorkers(skill);
            if (workers.workerRecords == null || !workers.workerRecords.Any())
            {
                return NotFound(workers);
            }
            return workers;
        }
    }
}
