using BusinessLogicLayer.AppLogic.JobRoom;
using BusinessLogicLayer.AppLogic.JobRoom.GetJobRoom;
using BusinessLogicLayer.AppLogic.JobRoom.SetRoomStatus;
using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Workers.GetWorkers;
using DataAccessLayer.AppLogic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.Infrastructure {
    public class JobRoomService : IJobRoomService {

        private IJobRoomRepository _repository;
        public JobRoomService(IJobRoomRepository jobRoomRepository)
        {
            this._repository = jobRoomRepository;
        }
        public GetJobRoomResponse GetJobRoomResponse(int jobID) {
            var query = _repository.GetJobRoom(jobID);
            if (query == null || !query.Any()) {
                return new GetJobRoomResponse() {
                    Error = "Ne postoji ovaj posao!"
                };
            }
            var jobRoom = query.Select(x => new JobRoomRecord() {
                JobId = x.JobId,
                WorkerId = x.WorkerId,
                JobTitle = x.JobTitle,
                SkillId = x.SkillId,
                SkillTitle = x.SkillTitle,
                JobStatus = x.JobStatus,
                EmployerId = x.EmployerId,
                WorkerName = x.WorkerName,
                WorkingId = x.WorkingId,
                AllowWorkerInvite = x.AllowWorkerInvite,
                WorkingStatus = x.WorkingStatus
                
            }).ToList();
            return new GetJobRoomResponse {
                JobRooms = jobRoom,
                Error = null
            };
        }

        public SetRoomStatusResponse SetRoomStatusResponse(int jobId, int status) {
            var query = _repository.SetRoomStatusModel(jobId, status);
            if(query == null || query.Error != null) {
                return new SetRoomStatusResponse() {
                    Error = query.Error
                };
            }
            return new SetRoomStatusResponse {
                Success = "Uspješno postavljen status sobe!"
            };


        }
    }
}
