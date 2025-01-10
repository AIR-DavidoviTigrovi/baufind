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
    public class WorkingRepository : IWorkingRepository
    {
        private readonly DB _db;

        public WorkingRepository(DB db)
        {
            _db = db;
        }

        public (bool, string) AddNewWorkingEntry(int workerId, int jobId, int skillId, int userId)
        {
            if (!ValidateUsersId(userId, jobId))
            {
                Console.WriteLine("Pristup odbijen: Korisnik nije vlasnik posla.");
                return (false, "Pristup odbijen: Korisnik nije vlasnik posla.");
            }
            if (IsWorkerAlreadyAssigned(workerId, jobId))
            {
                Console.WriteLine("Radnik je već dodijeljen za ovaj posao.");
                return (false, "Radnik je već dodijeljen za ovaj posao.");
            }
            if (!IsSkillValidForJob(skillId, jobId))
            {
                Console.WriteLine($"Skill_id {skillId} nije potreban za posao {jobId}.");
                return (false, $"Skill {skillId} nije potreban za vaš posao.");
            }
            string query = @"
        INSERT INTO Working (worker_id, skill_id, job_id, working_status_id)
        VALUES (@workerId, @skillId, @jobId, 3);";

            var parameters = new Dictionary<string, object>
                {
                    { "@workerId", workerId },
                    { "@skillId", skillId },
                    { "@jobId", jobId }
                };

            try
            {
                return (_db.ExecuteNonQuery(query, parameters) > 0, "");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Greška prilikom dodavanja novog zapisa: {ex.Message}");
                return (false, "Greška prilikom dodavanja novog zapisa") ;
            }
        }
        private bool IsSkillValidForJob(int skillId, int jobId)
        {
            string query = @"
            SELECT COUNT(*)
            FROM Working
            WHERE job_id = @jobId AND skill_id = @skillId;";

            var parameters = new Dictionary<string, object>
            {
                { "@jobId", jobId },
                { "@skillId", skillId }
            };

            try
            {
                var result = _db.ExecuteScalar(query, parameters);
                return Convert.ToInt32(result) > 0;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Greška prilikom provjere validnosti skillova: {ex.Message}");
                return false;
            }
        }


        private bool ValidateUsersId(int userId, int jobId)
        {
            string query = @"
            SELECT COUNT(*)
            FROM Job
            WHERE id = @jobId AND employer_id = @userId";
            var parameters = new Dictionary<string, object>
            {
                { "@jobId", jobId },
                { "@userId", userId }
            };
            using (var reader = _db.ExecuteReader(query, parameters))
            {
                if (reader.Read()) 
                {
                    int result = reader.GetInt32(0); 
                    return result > 0; 
                }
            }
            return false;
        }



        private bool IsWorkerAlreadyAssigned(int workerId, int jobId)
        {
            string query = @"
        SELECT COUNT(*)
        FROM Working
        WHERE worker_id = @workerId AND job_id = @jobId;";

            var parameters = new Dictionary<string, object>
    {
        { "@workerId", workerId },
        { "@jobId", jobId }
    };

            try
            {
                var result = _db.ExecuteScalar(query, parameters);
                return Convert.ToInt32(result) > 0; 
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Greška prilikom provjere dodjele radnika: {ex.Message}");
                return true; 
            }
        }



        private WorkingModel WorkingModelFromReader(SqlDataReader reader)
        {
            return new WorkingModel()
            {
                Id = (int)reader["id"],
                WorkerId = reader["worker_id"] == DBNull.Value ? (int?)null : (int)reader["worker_id"],
                SkillId = (int)reader["skill_id"],
                JobId = (int)reader["job_id"],
                WorkingStatusId = (int)reader["working_status_id"]
            };

        }

        public List<JobNotificationModel> GetPendingInvitations(int workerId)
        {
            var jobs = GetJobs(workerId); 
            var pictures = GetPictures(workerId); 
            var skills = GetSkills(workerId); 

            foreach (var job in jobs)
            {
                if (pictures.ContainsKey(job.Id))
                {
                    job.Pictures = pictures[job.Id];
                }

                if (skills.ContainsKey(job.Id))
                {
                    job.Skills = skills[job.Id];
                }
            }

            return jobs;
        }

        public List<JobNotificationModel> GetJobs(int workerId)
        {
            string query = @"
                 SELECT 
                j.id AS job_id,
                j.employer_id,
                j.job_status_id,
                j.title,
                j.description,
                j.allow_worker_invite,
                j.location,
                j.lat,
                j.lng,
                w.working_status_id
                FROM working w
                INNER JOIN job j ON w.job_id = j.id
                LEFT JOIN worker_review wr ON wr.working_id = w.id
                WHERE (w.working_status_id = 3 OR w.working_status_id = 4)
                AND w.worker_id = @WorkerId
                AND wr.id IS NULL";

                    var parameters = new Dictionary<string, object>
            {
                { "@WorkerId", workerId }
            };

            var jobs = new List<JobNotificationModel>();

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                while (reader.Read())
                {
                    jobs.Add(new JobNotificationModel
                    {
                        Id = reader.GetInt32(reader.GetOrdinal("job_id")),
                        Employer_id = reader.GetInt32(reader.GetOrdinal("employer_id")),
                        Job_status_id = reader.GetInt32(reader.GetOrdinal("job_status_id")),
                        Title = reader.GetString(reader.GetOrdinal("title")),
                        Description = reader.GetString(reader.GetOrdinal("description")),
                        Allow_worker_invite = reader.GetBoolean(reader.GetOrdinal("allow_worker_invite")),
                        Location = reader.GetString(reader.GetOrdinal("location")),
                        Lat = reader.IsDBNull(reader.GetOrdinal("lat")) ? (decimal?)null : reader.GetDecimal(reader.GetOrdinal("lat")),
                        Lng = reader.IsDBNull(reader.GetOrdinal("lng")) ? (decimal?)null : reader.GetDecimal(reader.GetOrdinal("lng")),
                        Pictures = new List<byte[]>(),
                        Skills = new List<SkillModel>(),
                        Working_status_id = reader.GetInt32(reader.GetOrdinal("working_status_id")),
                    });
                }
            }

            return jobs;
        }
        public Dictionary<int, List<byte[]>> GetPictures(int workerId)
        {
            string query = @"
                SELECT 
                    w.job_id,
                    p.picture
                FROM working w
                INNER JOIN job_picture jp ON w.job_id = jp.job_id
                INNER JOIN picture p ON jp.picture_id = p.id
                WHERE w.working_status_id = 3 AND w.worker_id = @WorkerId";

                        var parameters = new Dictionary<string, object>
                {
                    { "@WorkerId", workerId }
                };

            var pictures = new Dictionary<int, List<byte[]>>();

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                while (reader.Read())
                {
                    int jobId = reader.GetInt32(reader.GetOrdinal("job_id"));
                    if (!pictures.ContainsKey(jobId))
                    {
                        pictures[jobId] = new List<byte[]>();
                    }
                    pictures[jobId].Add((byte[])reader["picture"]);
                }
            }

            return pictures;
        }
        public Dictionary<int, List<SkillModel>> GetSkills(int workerId)
        {
            string query = @"
                SELECT 
                    w.job_id,
                    s.id AS skill_id,
                    s.title AS skill_name
                FROM working w
                INNER JOIN skill s ON w.skill_id = s.id
                WHERE w.working_status_id = 3 AND w.worker_id = @WorkerId";

                        var parameters = new Dictionary<string, object>
                {
                    { "@WorkerId", workerId }
                };

            var skills = new Dictionary<int, List<SkillModel>>();

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                while (reader.Read())
                {
                    int jobId = reader.GetInt32(reader.GetOrdinal("job_id"));
                    if (!skills.ContainsKey(jobId))
                    {
                        skills[jobId] = new List<SkillModel>();
                    }
                    skills[jobId].Add(new SkillModel
                    {
                        Id = reader.GetInt32(reader.GetOrdinal("skill_id")),
                        Title = reader.GetString(reader.GetOrdinal("skill_name"))
                    });
                }
            }

            return skills;
        }
        public bool InsertWorkerRequestToWorking(int userId, int jobId, int skillId)
        {
            if (!IsSkillValidForJob(skillId, jobId))
            {
                return false;
            }
            if (!WorkerIsValidForSkill(userId, jobId, skillId))
            {
                return false;
            }
            string query = @"
                INSERT INTO Working (worker_id, skill_id, job_id, working_status_id)
                VALUES (@workerId, @skillId, @jobId, 2);";

            var parameters = new Dictionary<string, object>
                {
                    { "@workerId", userId },
                    { "@skillId", skillId },
                    { "@jobId", jobId }
                };

            try
            {
                return (_db.ExecuteNonQuery(query, parameters) > 0);
            }
            catch (Exception ex)
            {
                return false;
            }

        }

        private bool WorkerIsValidForSkill(int userId, int jobId, int skillId)
        {
            string query = $@"
                SELECT DISTINCT s.id, s.title FROM working w
                INNER JOIN skill s ON w.skill_id = s.id
                WHERE w.job_id = @jobId
                AND w.worker_id IS NULL
                AND s.id = @skillId
                AND s.id NOT IN (
                    SELECT w2.skill_id FROM working w2
                    WHERE w2.job_id = @jobId
                    AND w2.worker_id = @userId
                );";

            var parameters = new Dictionary<string, object>
            {
                { "@jobId", jobId },
                { "@skillId", skillId },
                { "@userId", userId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                return reader.Read();
            }
        }
    }
}
