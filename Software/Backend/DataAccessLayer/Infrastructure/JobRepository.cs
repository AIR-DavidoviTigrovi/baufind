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
        /// </summary>
        /// <param name="skillIds"></param>
        /// <returns>Vraća sve poslove koji imaju otvorene pozicije koje smo dali kroz argument funkcije</returns>
        public List<JobModel> GetJobsWhereSkillPositionsOpen(List<int> skillIds)
        {
            string skillIdsString = string.Join(",", skillIds);

            string query = $@"
                SELECT * FROM job
                WHERE id IN (
                    SELECT job_id FROM working
                    WHERE skill_id IN ({skillIdsString})
                    AND worker_id IS NULL
                );";

            using (var reader = _db.ExecuteReader(query))
            {
                var result = new List<JobModel>();
                while (reader.Read())
                {
                    result.Add(JobModelFromReader(reader));
                }

                return result;
            }
        }

        private JobModel JobModelFromReader(SqlDataReader reader)
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
    }
}
