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

        public bool CallWorkerToFirstAvailableEntry(int workerId, int jobId, int skillId, int userId)
        {

            if (!ValidateUsersId(userId, jobId))
            {
                Console.WriteLine("Pristup odbijen: Korisnik nije vlasnik posla.");
                return false;
            }

            var entries = GetWorkingEntriesByJobId(jobId);

            if (entries == null || !entries.Any())
            {
                Console.WriteLine("Nema dostupnih redaka za zadani posao.");
                return false;
            }

            var entryToUpdate = entries.FirstOrDefault(e => e.WorkerId == null && e.SkillId == skillId);
            if (entryToUpdate == null)
            {
                Console.WriteLine("Nema dostupnih redaka bez dodijeljenog radnika s odgovarajućim skill_id.");
                return false;
            }

            string query = @"
    UPDATE Working
    SET worker_id = @workerId,
        working_status_id = 3
    WHERE id = @entryId
      AND skill_id = @skillId;";

            var parameters = new Dictionary<string, object>
    {
        { "@workerId", workerId },
        { "@entryId", entryToUpdate.Id },
        { "@skillId", skillId }
    };

            try
            {
                return _db.ExecuteNonQuery(query, parameters) > 0; 
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Greška prilikom dodjeljivanja radnika: {ex.Message}");
                return false;
            }
        }



        public List<WorkingModel> GetWorkingEntriesByJobId(int jobId)
        {
            string query = "SELECT * FROM Working WHERE job_id = @jobId;";
            var parameters = new Dictionary<string, object>
                {
                    { "@jobId", jobId }
                };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                var result = new List<WorkingModel>();
                while (reader.Read())
                {
                    result.Add(WorkingModelFromReader(reader)); 
                }
                return result;
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
