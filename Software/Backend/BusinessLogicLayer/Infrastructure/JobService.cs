using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.Infrastructure
{
    public class JobService : IJobService
    {
        private readonly IJobRepository _jobRepository;
        private readonly IJwtService _jwtService;

        public JobService(IJobRepository jobRepository, IJwtService jwtService)
        {
            _jobRepository = jobRepository;
            _jwtService = jwtService;
        }

        public AddJobResponse AddJob(AddJobRequest request, int user_id)
        {
            var validator = new AddJobValidator(_jobRepository);
            var result = validator.Validate(request);
            if(!result.IsValid)
            {
                return new AddJobResponse()
                {
                    Error = result.Errors.First().ErrorMessage
                };
            }

            JobModel jobModel = new JobModel()
            {
                Title = request.Title,
                Description = request.Description,
                Allow_worker_invite = request.Allow_worker_invite,
                Lat = request.Latitude,
                Lng = request.Longitude,
                Employer_id = user_id,
                Location = "Fiksno zasad",
                Job_status_id = 1
            };

            int? job_id = _jobRepository.AddJob(jobModel);
            if (job_id == null)
            {
                return new AddJobResponse()
                {
                    Error = "Greška kod dodavanja posla"
                };
            }
            //sad sa job_id dalje radim sve
            //skillovi
            /*
             * uzimam svaki ID_skill i jedan job_id i radim nove upise
             * fiksni working_status npr 1
             */
            //slike
            /*
             * 
            */

            return new AddJobResponse()
            {
                Success = $"Posao uspješno dodan sa id-em {job_id}"
            };
        }
    }
}
