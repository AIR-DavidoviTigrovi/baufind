using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;
using BusinessLogicLayer.AppLogic.Skills;
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
        private readonly ISkillRepository _skillRepository;
        private readonly IJwtService _jwtService;

        public JobService(IJobRepository jobRepository, IJwtService jwtService, IPictureRepository pictureRepository, ISkillRepository skillRepository)
        {
            _jobRepository = jobRepository;
            _jwtService = jwtService;
            _pictureRepository = pictureRepository;
            _skillRepository = skillRepository;
        }

        public AddJobResponse AddJob(AddJobRequest request, int userId)
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
                Employer_id = userId,
                Location = "Fiksno zasad",
                Job_status_id = 1
            };

            int? jobId = _jobRepository.AddJob(jobModel);
            if (jobId == null)
            {
                return new AddJobResponse()
                {
                    Error = "Greška kod dodavanja posla"
                };
            }
            _jobRepository.CreatePositionsForJob(request.Skills, jobId.Value);
            _pictureRepository.AddJobPicture(jobId.Value, request.Pictures);

            return new AddJobResponse()
            {
                Success = $"Posao uspješno dodan sa id-em {jobId}"
            };
        }

        public GetJobsForCurrentUserResponse GetJobsForCurrentUser(int userId)
        {
            var query = _skillRepository.GetSkillsForUser(userId);
            if (query == null || !query.Any())
            {
                return new GetJobsForCurrentUserResponse()
                {
                    Error = "Nemate niti jedan skill!"
                };
            }
            var skillIds = query.Select(x => x.Id).ToList();

            var newQuery = _jobRepository.GetJobsWhereSkillPositionsOpen(skillIds, userId);
            if (newQuery == null || !newQuery.Any())
            {
                return new GetJobsForCurrentUserResponse()
                {
                    Error = "Nema poslova za vaše vještine!"
                };
            }
            var jobs = newQuery.Select(x => new JobSearchModel()
            {
                Id = x.Id,
                Title = x.Title,
                Description = x.Description,
                Allow_worker_invite = x.Allow_worker_invite,
                Lat = x.Lat,
                Lng = x.Lng,
                Location = x.Location,
                Job_status_id = x.Job_status_id,
                Employer_id = x.Employer_id
            }).ToList();

            foreach (var job in jobs)
            {
                job.Skills = _jobRepository.GetSkillsForJobWhereSkillPositionsOpen(job.Id);
            }

            foreach (var job in jobs)
            {
                job.Pictures = _jobRepository.GetPicturesForJobWhereSkillPositionsOpen(job.Id);
            }


            return new GetJobsForCurrentUserResponse()
            {
                Jobs = jobs
            };
        }
    }
}
