using Azure.Core;
using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Jobs.AddUserToJob;
using BusinessLogicLayer.AppLogic.Jobs.ConfirmWorker;
using BusinessLogicLayer.AppLogic.Jobs.GetAllJobsHistory;
using BusinessLogicLayer.AppLogic.Jobs.GetJob;
using BusinessLogicLayer.AppLogic.Jobs.GetJobHistory;
using BusinessLogicLayer.AppLogic.Jobs.GetJobsForCurrentUser;
using BusinessLogicLayer.AppLogic.Jobs.WorkerJoinJob;
using BusinessLogicLayer.AppLogic.PushNotifications;
using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using Google.Apis.Util;
using System.Net.Http.Headers;

namespace BusinessLogicLayer.Infrastructure
{
    public class JobService : IJobService
    {
        private readonly IJobRepository _jobRepository;
        private readonly IPictureRepository _pictureRepository;
        private readonly ISkillRepository _skillRepository;
        private readonly IWorkingRepository _workingRepository;
        private readonly IJwtService _jwtService;
        private readonly IPushNotificationService _pushNotificationService;
        private readonly IWorkerRepository _workerRepository;

        public JobService(IJobRepository jobRepository, IJwtService jwtService, IPictureRepository pictureRepository, ISkillRepository skillRepository, IWorkingRepository workingRepository, IPushNotificationService pushNotificationService,IWorkerRepository workerRepository)
        {
            _jobRepository = jobRepository;
            _jwtService = jwtService;
            _pictureRepository = pictureRepository;
            _skillRepository = skillRepository;
            _workingRepository = workingRepository;
            _pushNotificationService = pushNotificationService;
            _workerRepository = workerRepository;
        }

        public AddJobResponse AddJob(AddJobRequest request, int userId)
        {
            var validator = new AddJobValidator(_jobRepository);
            var result = validator.Validate(request);
            if (!result.IsValid)
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
                Success = $"Posao uspješno dodan sa id-em {jobId}",
                JobId = jobId.Value
            };
        }

        public CallWarkerToJobResponse CallWorkerToJob(CallWorkerToJobRequest request, int userId)
        {
            var (added, message) = _workingRepository.AddNewWorkingEntry(request.WorkerId, request.JobId, request.SkillId, userId);
            CallWarkerToJobResponse response = new CallWarkerToJobResponse();
            response.Success = added;
            if (added && message == "")
            {
                _pushNotificationService.SendPushNotification("Pozvani ste na posao!", "Pozvani ste na posao! Kliknite ovdje da bi ste vidjeli o kojem poslu se radi.", new Dictionary<string, string>
                {
                    { "changeRoute", "jobNotificationScreen" } // TODO: prebaciti na waiting room kad se implementira: $"jobRoom/{request.JobId}"
                }, request.WorkerId);
                response.Message = "Radnik uspjesno pozvan na posao";
            }
            else if (!added && message == "")
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
                job.Skills = _jobRepository.GetEmptySkillsWhichUserHasForJob(job.Id, skillIds, userId);
            }

            return new GetJobsForCurrentUserResponse()
            {
                Jobs = jobs
            };
        }

        public GetJobResponse GetJob(int jobId, int userId)
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

            var query = _skillRepository.GetSkillsForUser(userId);
            var skillIds = query.Select(x => x.Id).ToList();

            job.Skills = _jobRepository.GetEmptySkillsWhichUserHasForJob(jobData.Id, skillIds, userId);
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
            }
            catch (Exception ex)
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
        public WorkerRequestJoinResponse WorkerRequestJoin(WorkerRequestJoinRequest request, int userId)
        {
            var response = new WorkerRequestJoinResponse();

            try
            {
                var success = _workingRepository.InsertWorkerRequestToWorking(userId, request.JobId, request.SkillId);
                response.Success = success;
            }
            catch (Exception ex)
            {
                response.Message = $"Nije prošlo validaciju: {ex.Message}";
                response.Success = false;
            }
            return response;
        }



        public ConfirmWorkerResponse ConfirmWorkerRequest(ConfirmWorkerRequest request)
        {
            var response = new ConfirmWorkerResponse();

            try
            {
                var success = _workingRepository.ConfirmWorker(request.JobId, request.WorkerId, request.SkillId, request.WorkingStatusId);
                response.Message = success.Item2;
                response.Success= success.Item1;
            }
            catch (Exception ex)
            {
                response.Message = $"Nešto je pošlo krivo: {ex.Message}";
                response.Success = false;
            }
            return response;
        }

        public MyJobsNotificationResponse GetMyJobsNotifications(int EmployerId)
        {
            var response = new MyJobsNotificationResponse();

            try
            {
                var success = _workingRepository.GetPendingJobApplications(EmployerId);

                List<MyJobNotificationModel> workingModels = success.Select(x => new MyJobNotificationModel()
                {
                    WorkerId = x.WorkerId,
                    Name = x.Name,  
                    Address = x.Address,  
                    SkillId = x.SkillId,
                    JobId = x.JobId,
                    JobTitle = x.JobTitle,  
                    WorkingStatusId = x.WorkingStatusId,
                    Rating = x.Rating,  
                    Skill = x.Skill,
                    CompletedJobsCount = x.CompletedJobsCount 
                }).ToList();
                if (workingModels.Count == 0)
                {
                    response.Message = "Nemate nikakvih obavijesti";
                }
                else
                {
                    response.Message = "Uspešno učitane obavijesti";
                }

                response.NotificationModels = workingModels; 
            }
            catch (Exception ex)
            {
                response.Message = $"Nešto je pošlo krivo: {ex.Message}";
            }
            return response;
        }

        public GetAllJobsHistoryResponse GetAllJobsHistory(int userId)
        {
            var jobList = _jobRepository.GetAllJobsHistory(userId);

            if (jobList == null)
            {
                return new GetAllJobsHistoryResponse()
                {
                    Error = "Niste sudjelovali na nijednom poslu!"
                };
            }
            else
            {
                var jobHistoryRecords = jobList.Select(job => new AllJobsHistoryRecord
                {
                    JobId = job.JobId,
                    Title = job.Title,
                    Picture = job.Picture,
                    CompletionDate = job.CompletionDate,
                    IsOwner = job.IsOwner
                }).ToList();

                return new GetAllJobsHistoryResponse()
                {
                    Jobs = jobHistoryRecords
                };
            }
        }

        public GetJobHistoryResponse GetJobHistory(int jobId, int userId)
        {
            bool isValid = _jobRepository.CheckIfUserWorkedOrOwnedJob(jobId, userId);
            if (!isValid)
            {
                return new GetJobHistoryResponse()
                {
                    Error = "Niste sudjelovali na ovom poslu!"
                };
            }
            var jobData = _jobRepository.GetJobHistory(jobId);

            if (jobData == null)
            {
                return new GetJobHistoryResponse()
                {
                    Error = "Posao nije pronađen!"
                };
            }

            var jobHistory = new JobHistoryRecord()
            {
                Id = jobData.JobId,
                Title = jobData.JobTitle,
                Description = jobData.JobDescription,
                Location = jobData.JobLocation,
                OwnerName = jobData.JobOwnerName,
                Workers = jobData.Workers,
                Events = jobData.Events
            };

            return new GetJobHistoryResponse()
            {
                JobHistory = jobHistory
            };
        }

        public ConfirmWorkerResponse WorkerConfirmsJob(ConfirmWorkerRequest request, int EmployerIdForNotification) {
            var response = new ConfirmWorkerResponse();

            try {
                var worker = _workerRepository.GetWorker(request.WorkerId);
                if (worker == null) {
                    response.Message = "Radnik nije pronađen!";
                    response.Success = false;
                    return response;
                }

                var success = _workingRepository.WorkerConfirmJob(request.JobId, request.WorkerId, request.WorkingStatusId);
                response.Message = success.Item2;
                response.Success = success.Item1;
                if (success.Item1 == false) {
                    return response;
                }

                switch (request.WorkingStatusId) {
                    case 4: {
                    _pushNotificationService.SendPushNotification($"Radnik {worker.Name} je potvrdio posao!", $"Radnik je potvrdio posao! Kliknite ovdje da bi ste vidjeli o kojem poslu se radi.", new Dictionary<string, string>
                    {
                    { "changeRoute", $"jobRoom/{request.JobId}" } 
                }, EmployerIdForNotification);
                            break;
                        }
                    case 5: {
                            _pushNotificationService.SendPushNotification($"Radnik {worker.Name} je odbio posao!", $"Radnik je odbio vaš posao :( Kliknite ovdje da bi ste vidjeli o kojem poslu se radi.", new Dictionary<string, string>
                    {
                    { "changeRoute", $"jobRoom/{request.JobId}" }
                    }, EmployerIdForNotification);
                            break;
                        }
                }
                
                
                
            } catch (Exception ex) {
                response.Message = $"Nešto je pošlo krivo: {ex.Message}";
                response.Success = false;
            }
            return response;
        }
    }
}
