using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using Microsoft.Data.SqlClient;

namespace DataAccessLayer.Infrastructure
{
    public class JobRepository : IJobRepository
    {
        private readonly DB _db;
        public JobRepository(DB db)
        {
            _db = db;
        }

        public int? AddJob(JobModel jobModel)
        {
            string query = @"
                INSERT INTO job 
                (employer_id, job_status_id, title, description, allow_worker_invite, location, lat, lng)
                VALUES
                (@employer_id, @job_status_id, @title, @description, @allow_worker_invite, @location, @lat, @lng)
                SELECT CAST(SCOPE_IDENTITY() AS INT);
            ";

            var parameters = new Dictionary<string, object>
            {
                { "@employer_id", jobModel.Employer_id },
                { "@job_status_id", jobModel.Job_status_id },
                { "@title", jobModel.Title },
                { "@description", jobModel.Description },
                { "@allow_worker_invite", jobModel.Allow_worker_invite },
                { "@location", jobModel.Location },
                { "@lat", jobModel.Lat ?? (object)DBNull.Value },
                { "@lng", jobModel.Lng ??(object) DBNull.Value }
            };

            object? result = _db.ExecuteScalar(query, parameters);
            return result != null ? (int)result : null;
        }

        public void CreatePositionsForJob(List<int> skills, int job_id)
        {
            string query = @"
                INSERT INTO working
                (skill_id, job_id, working_status_id)
                VALUES
                (@skill_id, @job_id, @working_status_id);
            ";

            foreach (var skill_id in skills)
            {
                var parameters = new Dictionary<string, object>
                {
                    { "@skill_id", skill_id },
                    { "@job_id", job_id },
                    { "@working_status_id", 1 }
                };

                _db.ExecuteNonQuery(query, parameters);
            }
        }
        /// <summary>
        /// Uzima poslove koji traže pozicije koje se daju kroz argument, tj. gdje su te pozicije otvorene.
        /// Ne uzima poslove koji je korisnik koji traži poslove postavio
        /// </summary>
        /// <param name="skillIds"></param>
        /// <returns>Vraća sve poslove koji imaju otvorene pozicije koje smo dali kroz argument funkcije</returns>
        public List<JobSearchModel> GetJobsWhereSkillPositionsOpen(List<int> skillIds, int userId)
        {
            string skillIdsString = string.Join(",", skillIds);

            string query = $@"
                SELECT * FROM job
                WHERE id IN (
                    SELECT job_id FROM working
                    WHERE skill_id IN ({skillIdsString})
                    AND worker_id IS NULL
                )
                AND employer_id != @userId;";

            var parameters = new Dictionary<string, object>
            {
                { "@userId", userId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                var result = new List<JobSearchModel>();
                while (reader.Read())
                {
                    result.Add(JobModelFromReader(reader));
                }

                return result;
            }
        }

        private JobSearchModel JobModelFromReader(SqlDataReader reader)
        {
            return new JobSearchModel()
            {
                Id = (int)reader["id"],
                Employer_id = (int)reader["employer_id"],
                Job_status_id = (int)reader["job_status_id"],
                Title = (string)reader["title"],
                Description = (string)reader["description"],
                Allow_worker_invite = (bool)reader["allow_worker_invite"],
                Location = (string)reader["location"],
                Lat = reader["lat"] as decimal?,
                Lng = reader["lng"] as decimal?
            };
        }


        /// <summary>
        /// Dohvaća slike za posao čiji je ID dan
        /// </summary>
        /// <param name="jobIds"></param>
        /// <returns>Slike za posao</returns>
        public List<byte[]> GetPicturesForJob(int jobId)
        {
            string query = $@"
                SELECT p.picture FROM job j
                INNER JOIN job_picture jp ON j.id = jp.job_id
                INNER JOIN picture p ON jp.picture_id = p.id
                WHERE j.id = @jobId;";

            var parameters = new Dictionary<string, object>
            {
                { "@jobId", jobId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                var pictures = new List<byte[]>();
                while (reader.Read())
                {
                    pictures.Add((byte[])reader["picture"]);
                }
                return pictures;
            }
        }
        /// <summary>
        /// Dohvaća sve vještine za posao
        /// </summary>
        /// <param name="jobId"></param>
        /// <returns>Vještine za posao</returns>
        public List<SkillModel> GetEmptySkillsWhichUserHasForJob(int jobId, List<int> skillIds)
        {

            string skillIdsString = string.Join(",", skillIds);

            string query = $@"
                SELECT s.id, s.title FROM working w
                INNER JOIN skill s ON w.skill_id = s.id
                WHERE w.job_id = @jobId
                AND w.worker_id IS NULL
                AND s.id IN ({skillIdsString});";

            var parameters = new Dictionary<string, object>
            {
                { "@jobId", jobId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                var skills = new List<SkillModel>();
                while (reader.Read())
                {
                    skills.Add(new SkillModel
                    {
                        Id = (int)reader["id"],
                        Title = (string)reader["title"]
                    });
                }
                return skills;
            }
        }

        public JobModel GetJob(int jobId)
        {
            string query = @"
                SELECT * FROM job
                WHERE id = @jobId;";

            var parameters = new Dictionary<string, object>
            {
                { "@jobId", jobId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                if (reader.Read())
                {
                    return new JobModel()
                    {
                        Id = (int)reader["id"],
                        Employer_id = (int)reader["employer_id"],
                        Job_status_id = (int)reader["job_status_id"],
                        Title = (string)reader["title"],
                        Description = (string)reader["description"],
                        Allow_worker_invite = (bool)reader["allow_worker_invite"],
                        Location = (string)reader["location"],
                        Lat = reader["lat"] as decimal?,
                        Lng = reader["lng"] as decimal?
                    };
                }
                return null;
            }
        }

        /// <summary>
        /// Dohvaća job i working po korisniku i statusu
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="statusId"></param>
        /// <returns></returns>
        public List<JobWorkingModel> GetJobWorkingByUserAndStatus(int userId, int statusId)
        {
            string query = @"
            SELECT j.id as job_id, w.id as working_id, j.title, j.location, ws.id as status_id, ws.status, w.worker_id
            FROM job j LEFT JOIN working w
            ON j.id = w.job_id
            JOIN working_status ws
            ON ws.id = w.working_status_id
            WHERE w.worker_id = @userId
            AND ws.id = @statusId;
            ";

            var parameters = new Dictionary<string, object>
            {
                { "@userId", userId },
                { "@statusId", statusId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                var jobs = new List<JobWorkingModel>();
                while (reader.Read())
                {
                    jobs.Add(new JobWorkingModel
                    {
                        JobId = (int)reader["job_id"],
                        WorkingId = (int)reader["working_id"],
                        WorkerId = reader["worker_id"] as int?,
                        StatusId = (int)reader["status_id"],
                        Status = (string)reader["status"],
                        Title = (string)reader["title"],
                        Location = (string)reader["location"]
                    });
                }
                return jobs;
            }
        }

        /// <summary>
        /// Dohvaća poslove kojima je korisnik vlasnik ili je primljen na njih
        /// </summary>
        /// <param name="userId"></param>
        /// <returns></returns>
        public List<MyJobModel> GetMyJobsForUser(int userId)
        {
            string query = @"
            SELECT
	            j.id,
	            j.employer_id,
	            j.job_status_id,
	            s.status as job_status,
	            j.title,
	            j.description,
	            j.allow_worker_invite,
	            j.location,
	            j.lat,
	            j.lng,
	            CAST(CASE WHEN j.employer_id = @userId THEN 1 ELSE 0 END AS BIT) as user_is_employer
                FROM job j
                LEFT JOIN job_status s
                ON j.job_status_id = s.id
                WHERE j.employer_id = @userId
                OR j.id IN (
	                SELECT job_id FROM working
	                WHERE worker_id = @userId
                    AND working_status_id = 4
                );";

            var parameters = new Dictionary<string, object>
            {
                { "@userId", userId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                var jobs = new List<MyJobModel>();
                while (reader.Read())
                {
                    jobs.Add(new MyJobModel
                    {
                        Id = (int)reader["id"],
                        JobStatusId = (int)reader["job_status_id"],
                        JobStatus = (string)reader["job_status"],
                        Title = (string)reader["title"],
                        Description = (string)reader["description"],
                        AllowWorkerInvite = (bool)reader["allow_worker_invite"],
                        Location = (string)reader["location"],
                        Lat = reader["lat"] as decimal?,
                        Lng = reader["lng"] as decimal?,
                        UserIsEmployer = (bool)reader["user_is_employer"]
                    });
                }
                return jobs;
            }
        }
    }
}
