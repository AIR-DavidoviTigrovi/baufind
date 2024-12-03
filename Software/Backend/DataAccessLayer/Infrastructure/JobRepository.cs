using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
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
        public JobRepository(DB db) {
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
    }
}
