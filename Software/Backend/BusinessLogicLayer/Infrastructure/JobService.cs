﻿using BusinessLogicLayer.AppLogic;
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
        private readonly IPictureRepository _pictureRepository;
        private readonly IJwtService _jwtService;

        public JobService(IJobRepository jobRepository, IJwtService jwtService, IPictureRepository pictureRepository)
        {
            _jobRepository = jobRepository;
            _jwtService = jwtService;
            _pictureRepository = pictureRepository;
        }

        public AddJobResponse AddJob(AddJobRequest request, int user_id)
        {
            var validator = new AddJobValidator(_jobRepository);
            var result = validator.Validate(request);
            if(!result.IsValid)
            {
                return new AddJobResponse()
                {
                    Error = string.Join(Environment.NewLine, result.Errors.Select(e => e.ErrorMessage))
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
            _jobRepository.CreatePositionsForJob(request.Skills, job_id.Value);
            _pictureRepository.AddJobPicture(job_id.Value, request.Pictures);

            return new AddJobResponse()
            {
                Success = $"Posao uspješno dodan sa id-em {job_id}"
            };
        }
    }
}
