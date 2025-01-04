using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using Microsoft.Data.SqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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
        public List<SkillModel> GetSkillsForJob(int jobId)
        {
            string query = @"
                SELECT s.id, s.title FROM working w
                INNER JOIN skill s ON w.skill_id = s.id
                WHERE w.job_id = @jobId;";

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
    }
}
