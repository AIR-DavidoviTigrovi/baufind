using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Jobs.AddUserToJob;
using BusinessLogicLayer.AppLogic.Jobs.GetJob;
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
        private readonly IWorkingRepository _workingRepository;
        private readonly IJwtService _jwtService;

        public JobService(IJobRepository jobRepository, IJwtService jwtService, IPictureRepository pictureRepository, ISkillRepository skillRepository, IWorkingRepository workingRepository)
        {
            _jobRepository = jobRepository;
            _jwtService = jwtService;
            _pictureRepository = pictureRepository;
            _skillRepository = skillRepository;
            _workingRepository = workingRepository;
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
                Location = request.Location,
                Employer_id = userId,
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

        public CallWarkerToJobResponse CallWorkerToJob(CallWorkerToJobRequest request, int userId)
        {
            var (added, message) = _workingRepository.AddNewWorkingEntry(request.WorkerId, request.JobId, request.SkillId, userId);
            CallWarkerToJobResponse response = new CallWarkerToJobResponse();
            response.Success = added;
            if (added && message =="")
            {
                response.Message = "Radnik uspjesno pozvan na posao";
            }
            else if (!added && message =="")
            {
                response.Message = "Greska";
            }
            else
            {
                response.Message = message;
            }
            return response;
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
                job.Skills = _jobRepository.GetSkillsForJob(job.Id);
            }

            return new GetJobsForCurrentUserResponse()
            {
                Jobs = jobs
            };
        }

        public GetJobResponse GetJob(int jobId)
        {
            var jobData = _jobRepository.GetJob(jobId);

            if (jobData == null)
            {
                return new GetJobResponse()
                {
                    Error = "Posao nije pronađen!"
                };
            }

            var job = new FullJobRecord()
            {
                Id = jobData.Id,
                Title = jobData.Title,
                Description = jobData.Description,
                Allow_worker_invite = jobData.Allow_worker_invite,
                Lat = jobData.Lat,
                Lng = jobData.Lng,
                Location = jobData.Location,
                Job_status_id = jobData.Job_status_id,
                Employer_id = jobData.Employer_id
            };

            job.Skills = _jobRepository.GetSkillsForJob(jobData.Id);
            job.Pictures = _jobRepository.GetPicturesForJob(jobData.Id);

            return new GetJobResponse()
            {
                Job = job
            };
        }

        public PendingInvitationResponse GetPendingInvitations(int userId)
        {
            var response = new PendingInvitationResponse();

            try
            {
                var jobs = _workingRepository.GetPendingInvitations(userId);
                response.Jobs = jobs;
            }
            catch (Exception ex)
            {
                response.Error = $"Došlo je do greške prilikom dohvaćanja podataka: {ex.Message}";
            }

            return response;
        }

        public SearchPendingJobsForUserResponse SearchPendingJobsForUser(int userId)
        {
            var response = new SearchPendingJobsForUserResponse();

            try
            {
                var jobs = new List<JobWorkingModel>();
                int[] statuses = [2, 3, 4];
                foreach (var status in statuses)
                {
                    var foundJobsForStatus = _jobRepository.GetJobWorkingByUserAndStatus(userId, status);
                    jobs.AddRange(foundJobsForStatus);
                }
                response.Jobs = jobs;
                response.Success = true;
            } catch (Exception ex)
            {
                response.Error = $"Došlo je do greške prilikom dohvaćanja podataka: {ex.Message}";
                response.Success = false;
            }

            return response;
        }

        public SearchMyJobsForUserResponse SearchMyJobsForUser(int userId)
        {
            var response = new SearchMyJobsForUserResponse();

            try
            {
                var jobs = _jobRepository.GetMyJobsForUser(userId);
                response.Jobs = jobs;
                response.Success = true;
            }
            catch (Exception ex)
            {
                response.Error = $"Došlo je do greške prilikom dohvaćanja podataka: {ex.Message}";
                response.Success = false;
            }

            return response;
        }
    }
}
