using BusinessLogicLayer.AppLogic.Users.GetAllUsers;
using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.AppLogic.Workers;
using BusinessLogicLayer.AppLogic.Workers.GetWorkers;
using DataAccessLayer.AppLogic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DataAccessLayer.Models;

namespace BusinessLogicLayer.Infrastructure {
    public class WorkersService : IWorkersService {

        private IWorkerRepository _workerRepository;

        public WorkersService(IWorkerRepository workerRepository)
        {
            this._workerRepository = workerRepository;   
        }
        public GetWorkersResponse GetWorkers(string skill) 
        {
            var query = _workerRepository.GetWorkers(skill);

            if (query == null || !query.Any()) {
                return new GetWorkersResponse() {
                    Error = "U bazi nema korisnika!"
                };
            }

            var workers = query.Select(x => new WorkerRecord() {
                Id = x.Id,
                Name = x.Name,
                Address = x.Address,
                NumOfJobs = x.NumOfJobs,
                Skills = x.Skills,
                AvgRating = x.AvgRating
            }).ToList();

            return new GetWorkersResponse() {
                WorkerRecords = workers
            };

        }
    }
}
