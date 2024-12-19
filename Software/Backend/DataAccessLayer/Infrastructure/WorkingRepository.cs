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

    }
}
