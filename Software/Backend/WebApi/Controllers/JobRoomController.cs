using BusinessLogicLayer.AppLogic.JobRoom;
using BusinessLogicLayer.AppLogic.JobRoom.GetJobRoom;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers {
    [Route("jobRoom")]
    [ApiController]
    public class JobRoomController : ControllerBase {
        
        private readonly IJobRoomService _jobRoomService;
        public JobRoomController(IJobRoomService jobRoomService)
        {
            this._jobRoomService = jobRoomService;
            
        }
        // GET: /jobRoom/{jobID}
        [HttpGet("{jobID}")]
        [Authorize]
        public ActionResult<GetJobRoomResponse> GetJobRoom(int jobID) {
            var query = _jobRoomService.GetJobRoomResponse(jobID);
            if (query.JobRooms == null || !query.JobRooms.Any()) {
                return NotFound(query);
            }
            return query;
        }
    }
}
